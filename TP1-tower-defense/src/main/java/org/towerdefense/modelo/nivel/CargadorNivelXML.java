package org.towerdefense.modelo.nivel;

import org.towerdefense.modelo.Posicion;
import org.towerdefense.modelo.Ruta;
import org.towerdefense.modelo.Slot;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CargadorNivelXML {
    public Nivel cargar(int numero, InputStream xml, InputStream xsd) {
        try {
            byte[] xmlBytes = xml.readAllBytes();

            SchemaFactory factoryXSD = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema esquema = factoryXSD.newSchema(new StreamSource(xsd));
            Validator validador = esquema.newValidator();
            validador.validate(new StreamSource(new ByteArrayInputStream(xmlBytes)));

            DocumentBuilderFactory factoryDOM = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factoryDOM.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlBytes));
            Element nivel = doc.getDocumentElement();

            int dinero = Integer.parseInt(nivel.getAttribute("dineroInicial"));
            int vida = Integer.parseInt(nivel.getAttribute("vidaBase"));

            Ruta ruta = parsearRuta(nivel);
            List<Slot> slots = parsearSlots(nivel);
            List<TorretaInicial> iniciales = parsearTorretasIniciales(nivel);
            Oleada oleada = parsearOleada(nivel);

            return new Nivel(numero, dinero, vida, ruta, slots, iniciales, oleada);
        } catch (Exception e) {
            throw new NivelInvalidoException("Error en el formato o lectura del nivel: " + e.getMessage(), e);
        }
    }

    private List<TorretaInicial> parsearTorretasIniciales(Element nivel) {
        List<TorretaInicial> iniciales = new ArrayList<>();
        NodeList contenedores = nivel.getElementsByTagName("torretasIniciales");
        if (contenedores.getLength() == 0) {
            return iniciales;
        }
        Element elIniciales = (Element) contenedores.item(0);
        NodeList listaTorretas = elIniciales.getElementsByTagName("torreta");
        for (int i = 0; i < listaTorretas.getLength(); i++) {
            Element t = (Element) listaTorretas.item(i);
            String tipo = t.getAttribute("tipo");
            double x = Double.parseDouble(t.getAttribute("x"));
            double y = Double.parseDouble(t.getAttribute("y"));
            iniciales.add(new TorretaInicial(tipo, new Posicion(x, y)));
        }
        return iniciales;
    }

    private Ruta parsearRuta(Element nivel) {
        Element elRuta = (Element) nivel.getElementsByTagName("ruta").item(0);
        NodeList listaPuntos = elRuta.getElementsByTagName("punto");

        List<Posicion> posiciones = new ArrayList<>();
        for (int i = 0; i < listaPuntos.getLength(); i++) {
            Element p = (Element) listaPuntos.item(i);
            double x = Double.parseDouble(p.getAttribute("x"));
            double y = Double.parseDouble(p.getAttribute("y"));
            posiciones.add(new Posicion(x, y));
        }
        return new Ruta(posiciones);
    }

    private List<Slot> parsearSlots(Element nivel) {
        Element elSlots = (Element) nivel.getElementsByTagName("slots").item(0);
        NodeList listaSlots = elSlots.getElementsByTagName("slot");

        List<Slot> slots = new ArrayList<>();
        for (int i = 0;  i < listaSlots.getLength(); i++) {
            Element s = (Element) listaSlots.item(i);
            double x = Double.parseDouble(s.getAttribute("x"));
            double y = Double.parseDouble(s.getAttribute("y"));
            slots.add(new Slot(new Posicion(x, y)));
        }
        return slots;
    }

    private Oleada parsearOleada(Element nivel) {
        Element elOleada = (Element) nivel.getElementsByTagName("oleada").item(0);
        NodeList listaEnemigos = elOleada.getElementsByTagName("enemigo");

        List<SpawnProgramado> spawns = new ArrayList<>();
        for (int i = 0; i < listaEnemigos.getLength(); i++) {
            Element e = (Element) listaEnemigos.item(i);
            long delayMs = Long.parseLong(e.getAttribute("delayMs"));
            String tipo = e.getAttribute("tipo");
            spawns.add(new SpawnProgramado(delayMs, tipo));
        }

        spawns.sort((a, b) -> Long.compare(a.delayMs(), b.delayMs()));
        return new Oleada(spawns);
    }
}
