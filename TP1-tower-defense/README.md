# Tower Defense - TP1

**Facultad:** Facultad de Ingeniería, Universidad de Buenos Aires (FIUBA)  
**Materia:** Paradigmas de Programación

**Grupo:** Paradelo-Zarate  
**Integrantes:**
- Ezequiel Paradelo
- Agustín Zarate

**Docentes y Corrector:** 
- Diego Nicolás Essaya
- Corrector: Santiago Maraggi

---

## Descripción del proyecto
Implementación de un juego "Tower Defense", desarrollado en Java utilizando el paradigma de 
Programación Orientada a Objetos (POO) y JavaFX para la interfaz gráfica. 
El proyecto cuenta con una separación entre el Modelo lógico y la Vista. Durante toda 
la implementación se respeta los principios de diseño SOLID y diferentes Principios de Diseño.

---
## Reglas del juego
### Objetivo
Sobrevivir a las oleadas enemigas impidiendo que lleguen a la base. Para lograrlo, hay que comprar 
y colocar estratégicamente diferentes tipos de torretas a lo largo del mapa durante las oleadas.

### Instrucciones de juego
1. **Inicio:** Empezás con una cantidad de dinero base. Al iniciar el nivel, los enemigos van a empezar 
a salir de su base siguiendo la ruta marcada en el mapa.
2. **Torretas:** Se puede hacer click en los íconos de las torretas para seleccionarla. Luego, haciendo click
en un `Slot` vacío (marcado en el mapa) para instalarla. Al hacerlo te restará el dinero correspondiente.
Existen diferentes tipos de torretas:
   - *Torreta Simple:* Disparo normal.
   - *Torreta Rápida:* Doble cadencia de disparo.
   - *Torreta Potente:* Doble de daño, color de proyectil distinto.  
Podés reemplazar una torreta existente seleccionando una nueva e instalándola sobre el mismo slot.
4. **Economía:** Al derrotar enemigos (Débil, Medio o Fuerte), ganás dinero y puntos.
5. **Derrota/Victoria:** 
   - Perdés si la vida de tu base llega a 0.
   - Ganás el nivel al sobrevivir todas las oleadas. Superando los tres niveles, se consigue la victoria definitiva.
---

## Instrucciones y requisitos de ejecución
### Requisitos
Tener Java y Maven instalado

### Ejecución
Clonar el repositorio y ubicarse en la carpeta raíz del proyecto (`TP1-tower-defense`).  
Ejecutar:
```
mvn javafx:run
```

---

## Formato de carga de niveles (.xml)

Los niveles son cargados al iniciar la partida mediante archivos XML validados con su respectivo XSD. 
El formato `.xml` de niveles es el siguiente:
```xml
<nivel dineroInicial="100" vidaBase="10">
    <ruta>
        <punto x="0" y="300"/>
        <punto x="800" y="300"/>
    </ruta>
    <torretasIniciales/>
    <slots>
        <slot x="200" y="200"/>
        <slot x="400" y="400"/>
    </slots>
    <oleada>
        <enemigo tipo="debil" delayMs="2000"/>
        <enemigo tipo="fuerte" delayMs="5000"/>
    </oleada>
</nivel>
```
Los niveles comienzan con dos variables, `dineroInicial` que es el dinero base con el que se empieza el nivel y `vidaBase` 
que representa la vida inicial de la base del jugador.

Los puntos de la ruta representan las "esquinas" del camino donde los enemigos cambian de dirección.
Se considera que el primer punto de ruta indica el `spawn` de enemigos, y el último punto indica la `base`
del jugador. 

Las `torretasIniciales` son torretas que vienen preinstaladas en el nivel. Deben coincidir con algún `slot`.

Para las oleadas, en cada enemigo se especifica su `tipo` y `delayMs`. El *delay* (tiempo de espera) es absoluto, es decir, 
los milisegundos de cada enemigo empiezan a contar desde que empieza el nivel.

---

## Licencias
- Spawning by NearTheAtmoshphere -- https://freesound.org/s/683184/ -- License: Creative Commons 0
- Kill Enemy 5 8 Bit.wav by Mrthenoronha -- https://freesound.org/s/506584/ -- License: Attribution NonCommercial 4.0
- sfx_win_3.wav by Mihacappy -- https://freesound.org/s/844146/ -- License: Creative Commons 0
- WinMutedGuitar.wav by Fupicat -- https://freesound.org/s/521646/ -- License: Creative Commons 0
- Place.wav by ryusa -- https://freesound.org/s/467563/ -- License: Attribution 4.0
- Game music loop #7 by BloodPixelHero -- https://freesound.org/s/610068/ -- License: Attribution 4.0
- Sound Effect by https://pixabay.com/users/freesound_community-46691455/ from Pixabay
- Los sprites y assets son los provistos por la cátedra.