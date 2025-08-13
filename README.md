# 🏥 SLG - Sistema de Gestión de Citas Médicas

## 📋 Descripción del Proyecto

**SLG-Citas** es una aplicación de escritorio desarrollada en Java que gestiona el sistema de citas médicas para una clínica odontológica. El sistema permite administrar citas, pacientes y usuarios con un enfoque híbrido que combina almacenamiento local y sincronización con APIs externas.

## 🚀 Tecnologías y Habilidades Técnicas

### **Lenguaje y Plataforma**
- **Java 11** - Desarrollo principal del backend
- **JavaFX 17** - Interfaz gráfica de usuario moderna
- **Maven** - Gestión de dependencias y build automation

### **Frameworks y Librerías**
- **JFoenix 9.0** - Componentes UI Material Design para JavaFX
- **FontAwesomeFX 8.9** - Iconografía profesional
- **EclipseLink (JPA 2.2)** - Persistencia de datos y ORM
- **iText PDF 7.0** - Generación de reportes en PDF
- **Retrofit2** - Cliente HTTP para APIs REST
- **JSON** - Procesamiento de datos JSON

### **Arquitectura y Patrones**
- **MVC (Model-View-Controller)** - Separación clara de responsabilidades
- **JPA/Hibernate** - Mapeo objeto-relacional
- **Singleton Pattern** - Gestión de conexiones y recursos
- **Threading** - Procesamiento asíncrono y manejo de UI
- **Arquitectura Híbrida** - Combinación de almacenamiento local y APIs externas

## 🏗️ Estructura del Proyecto

```
src/main/java/
├── controller/           # Controladores principales de la aplicación
├── controllerDoctor/     # Gestión de doctores
├── controllerLogin/      # Sistema de autenticación
├── Entidades/           # Modelos de datos (JPA Entities)
├── EntidadesAux/        # Entidades auxiliares
├── Pdf/                 # Generación de reportes PDF
├── Perspectiva/         # Vistas específicas por rol
└── Util/                # Utilidades y helpers
```

## 💼 Funcionalidades Principales

### **Gestión de Citas**
- ✅ Crear, modificar y eliminar citas médicas
- ✅ Calendario de citas con interfaz intuitiva
- ✅ Asignación de doctores y horarios
- ✅ Gestión de ubicaciones (Huamanga, Huanta, Ortognática)

### **Administración de Usuarios**
- 🔐 Sistema de login con roles diferenciados
- 👨‍⚕️ Gestión de doctores y especialidades
- 👥 Registro y gestión de pacientes
- 📍 Control de acceso por ubicación

### **Reportes y Documentación**
- 📄 Generación de horarios en PDF
- 🖨️ Impresión de citas y reportes
- 📊 Visualización de estadísticas

### **Conectividad y APIs**
- 🌐 **APIs REST** - Comunicación con servidor central para autenticación y sesiones
- 🏠 **Base de datos local** - PostgreSQL para almacenamiento principal de datos
- 🔐 **Autenticación centralizada** - Tokens CSRF y cookies de sesión
- 📡 **HTTP/2** - Comunicación eficiente con servidor externo
- 🔍 **API RENIEC** - Validación de DNI mediante servicio externo

## 🎯 Habilidades Demostradas

### **Desarrollo Backend**
- Programación orientada a objetos en Java
- Manejo de bases de datos con JPA
- Implementación de APIs REST
- Gestión de transacciones y persistencia

### **Interfaz de Usuario**
- Diseño de interfaces modernas con JavaFX
- Implementación de patrones UI/UX
- Responsive design y navegación intuitiva
- Integración de iconografía y elementos visuales

### **Arquitectura de Software**
- Diseño modular y escalable
- Separación de responsabilidades (MVC)
- Manejo de estados y navegación
- Implementación de patrones de diseño

### **Herramientas de Desarrollo**
- Control de versiones con Git
- Build automation con Maven
- Gestión de dependencias
- Testing y debugging

## 🛠️ Requisitos del Sistema

- **Java 11** o superior
- **Maven 3.8+**
- **PostgreSQL** - Base de datos local
- **Conexión a internet** - Para autenticación y validaciones
- **Sistema operativo**: Windows, macOS, Linux

