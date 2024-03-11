# Proyecto de Búsqueda Laboral Local

Este proyecto es una plataforma de búsqueda laboral local, similar a LinkedIn pero enfocada en una comunidad específica. Permite a los usuarios registrados buscar empleo, publicar ofertas de trabajo y establecer conexiones profesionales dentro de su área local.

## Características

- **Registro de Usuarios:** Los usuarios pueden registrarse como empleados o empleadores.
- **Perfiles de Empleados:** Los empleados pueden crear perfiles con información sobre su experiencia laboral, educación y habilidades.
- **Publicación de Puestos de Trabajo:** Los empleadores pueden publicar ofertas de trabajo con detalles sobre el puesto y los requisitos.
- **Postulaciones:** Los empleados pueden postularse a los puestos de trabajo publicados por los empleadores.
- **Administración del Sistema:** Los administradores tienen acceso a estadísticas y herramientas de administración para gestionar el sistema.

## Tecnologías Utilizadas

- **Back-end:** Spring Boot con Java
- **Base de Datos:** MySQL
- **Front-end:** Vue3

## Configuración del Proyecto

1. Clona este repositorio en tu máquina local.
2. Configura tu base de datos MySQL y actualiza las credenciales en el archivo `application.properties`.
3. Ejecuta la aplicación Spring Boot.
4. Accede a la aplicación a través de tu navegador web.

## Escenarios de Given-When-Then

### 1. Creación de Perfil Público de Empleado

**Given:** Un empleado registrado decide crear un perfil y elige la opción de hacerlo público.

**When:** El empleado completa su perfil con información sobre su experiencia laboral, educación y habilidades, y selecciona la opción de hacer su perfil público.

**Then:** El sistema almacena la información del perfil del empleado y lo marca como público, permitiendo que otros usuarios puedan ver su información.

### 2. Publicación de Puesto de Trabajo por Empleador

**Given:** Un empleador registrado desea publicar un nuevo puesto de trabajo en el sistema.

**When:** El empleador completa un formulario con detalles sobre el puesto, incluyendo título, descripción, requisitos y fecha de cierre.

**Then:** El sistema registra el nuevo puesto de trabajo en la base de datos, asociándolo con el empleador que lo publicó. El puesto de trabajo se muestra en la lista de puestos disponibles para los candidatos.

### 3. Postulación de Empleado a Puesto de Trabajo

**Given:** Un empleado registrado encuentra un puesto de trabajo que le interesa y decide postularse.

**When:** El empleado selecciona el puesto de trabajo y completa el formulario de solicitud adjuntando su currículum.

**Then:** El sistema registra la postulación del empleado para el puesto de trabajo seleccionado y notifica al empleador correspondiente sobre la nueva postulación.

## Licencia

Este proyecto está bajo la licencia [MIT License](LICENSE).
