# Pixelfy 🎨
<img width="2816" height="1536" alt="Logo_Pixelfy" src="https://github.com/user-attachments/assets/bb172a3d-03bc-4662-90bf-6e6d5c07191b" />

**Pixelfy** es un software de dibujo digital de escritorio diseñado específicamente para la creación de "Pixel Art". Este proyecto fue desarrollado como aplicación práctica de los principios de la Programación Orientada a Objetos (POO) utilizando Java.

## 🎯 El Problema que Resuelve
Las herramientas de dibujo digital profesionales (como Gimp, Clip Studio Paint o Krita) suelen presentar interfaces complejas y saturadas de funciones que resultan abrumadoras para los principiantes, especialmente para aquellos enfocados en el Pixel Art. 

Pixelfy resuelve esto ofreciendo un entorno:
- **Especializado:** Contiene únicamente las herramientas esenciales para Pixel Art, eliminando configuraciones innecesarias de resolución y catálogos excesivos de pinceles.
- **Autónomo:** Operación 100% local sin necesidad de conexión a internet.
- **Escalable:** Construido bajo una arquitectura POO que permite incorporar nuevas herramientas sin reestructurar el núcleo del programa.

## ⚙️ Características Principales
El sistema opera bajo un flujo de trabajo de Entradas, Procesos y Salidas:
- **Herramientas de Dibujo Interactivas:** Implementación de Lápiz, Borrador y Cubeta de relleno (con algoritmo de áreas adyacentes).
- **Lienzo Dinámico:** Matriz interactiva de casillas que responde a eventos del usuario.
- **Persistencia de Datos:** Capacidad de guardar y cargar proyectos de forma local garantizando la integridad de los datos.
- **Manejo Seguro de Errores:** Implementación de bloques `try-catch` para evitar cierres inesperados durante la lectura/escritura de archivos.

## 🛠️ Arquitectura y Tecnologías
- **Lenguaje:** Java 
- **Interfaz Gráfica:** Java Swing (`JFrame`, `JPanel`, `JButton`, Eventos `ActionListener`).
- **Patrón Arquitectónico:** Separación en paquetes `modelo` (lógica y datos) y `vista` (interfaz gráfica GUI).

### Conceptos de POO Implementados
El diseño del software se basa sólidamente en los pilares de la POO:
- **Herencia y Clases Abstractas:** Jerarquía de herramientas donde `Lapiz`, `Borrador` y `Cubeta` heredan de la clase abstracta `Herramienta`.
- **Polimorfismo:** Ejecución dinámica de la herramienta seleccionada sobreescribiendo el método `aplicar()` sin necesidad de condicionales en la vista.
- **Interfaces:** Estandarización de la persistencia de datos mediante la interfaz `Archivable` (implementada por `GestorArchivos`).
- **Composición y Agregación:** El `Lienzo` se compone de múltiples objetos `Casilla`. La `VentanaPrincipal` interactúa dinámicamente con las herramientas y el gestor de archivos.
- **Encapsulamiento:** Protección de atributos internos y comunicación exclusiva a través de métodos de acceso (Getters y Setters).

## 👥 Equipo de Desarrollo
- Mendez Baas, Angel Said
- Novelo Uc, Ángel Santiago
- Quintal Moo, Alejandro Emmanuel
- Trujeque Martin, Isaac Alejandro
