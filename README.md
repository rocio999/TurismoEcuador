### GUIA_TURISTICA
```text
Guía de Sitios Turísticos

AppTurismo es una aplicación móvil para Android creada en Kotlin para ayudar a explorar sitios turísticos.

La app permite buscar destinos, filtrar lugares, ver información detallada, localizar ubicaciones y guardar sitios preferidos.

//Propósito//

La meta de AppTurismo es proporcionar una herramienta móvil sencilla para descubrir destinos turísticos y acceder a información relevante sobre cada sitio.
 //Características//

-  Visualización de sitios turísticos.
-  Búsqueda de lugares.
- ️ Filtrado por categorías.
-  Detalles informativos de cada sitio.
-  Visualización de opiniones.
-  Obtención de la ubicación del usuario.
-  Localización de destinos próximos.
- ️ Registro de sitios favoritos.
-️ Exhibición de fotos.
-  Consulta de información a través de servicios en línea.
-  Almacenamiento local de datos y preferencias.

   //Tecnologías utilizadas//

- Kotlin
- Jetpack Compose
- Android Studio
- Arquitectura MVVM
- Room Database
- DataStore
- StateFlow
- Coroutines
- Retrofit
- OkHttp
- Coil
- Google Play Services Location

//Arquitectura//

La aplicación adopta el patrón arquitectónico MVVM (Modelo-Vista-ViewModel), lo que facilita la separación entre la interfaz de usuario, la lógica de presentación y la gestión de datos.

La estructura fundamental del proyecto incluye:

- UI: alberga las pantallas y los componentes creados con Jetpack Compose.
- ViewModel: se encarga de manejar el estado de la interfaz y coordina las acciones dentro de la aplicación.
- Repository: se ocupa del acceso y la interacción con las diversas fuentes de datos.
- Room: permite guardar datos de manera local, incluyendo los lugares favoritos.
- DataStore: se utiliza para almacenar las preferencias del usuario en la aplicación.
- Remote: alberga los servicios necesarios para obtener datos de APIs externas.

###  Estructura del proyecto

com.example.appturismo
├── data.database
│   ├── datastore
│   │   └── preferences
│   │       └── PreferenciasDataStore.kt
│   ├── model
│   │   └── Destino
│   ├── remote
│   │   ├── ApiService
│   │   ├── ApiTest.kt
│   │   ├── ApiTestViewModel
│   │   ├── DestinoApi
│   │   ├── DestinoApiResponse.kt
│   │   ├── DestinoRemoteRepository
│   │   ├── ImagenApiClient
│   │   ├── ImagenApiService.kt
│   │   └── RetrofitClient
│   ├── repository
│   │   ├── DestinoRepository
│   │   └── FavoritoRepository
│   ├── DatabaseProvider
│   ├── Favorito
│   ├── FavoritoDao
│   └── FavoritoDatabase
│
├── ui.theme
│   ├── components
│   │   └── DestinoCard.kt
│   ├── di
│   │   └── AppContainer.kt
│   ├── screens
│   │   ├── AjustesScreen.kt
│   │   ├── DestinosScreen.kt
│   │   ├── DetalleDestinoScreen.kt
│   │   ├── FavoritosScreen.kt
│   │   ├── HomeScreen.kt
│   │   └── UbicacionScreen.kt
│   ├── Color.kt
│   ├── Navigation.kt
│   ├── Theme.kt
│   └── Type.kt
│
├── viewmodel
│   ├── DestinoViewModel.kt
│   ├── FavoritoViewModel
│   ├── FavoritoViewModelFactory
│   ├── ImagenTestViewModel
│   ├── PreferenciasViewModel
│   ├── PreferenciasViewModelFactory
│   └── UbicacionViewModel
│
├── AppTurismoApp
└── MainActivity

//Organización del proyecto//
--Datos

Incluye los elementos relacionados con la gestión y acceso a la información de la aplicación.

--Modelo

Comprende los modelos de datos que utiliza la aplicación.

Destino
--Almacenamiento de Datos

Se emplea Almacenamiento de Datos para guardar las preferencias de la aplicación.

Incluye:

--Preferencias
--DatosPreferencias
--Base de Datos Room

Room se usa para administrar el almacenamiento local.

Contiene componentes como:

Favoritos
DaoFavoritos
BaseDatosFavoritos
ProveedorBaseDatos
--Repositorio

Agrupa los repositorios que se encargan del acceso a la información.

--Remoto

Incluye los componentes empleados para acceder a servicios y fuentes externas de datos.

--Interfaz de usuario

La interfaz se ha creado usando Jetpack Compose.

En el tema de ui se encuentran:

componentes/ → elementos reutilizables.
pantallas/ → vistas de la aplicación.
di/ → configuración de dependencias.
Navegación. kt → gestión de la navegación entre vistas.
Color. kt → paleta de colores.
Tema. kt → diseño de la aplicación.
Tipo. kt → estilos de texto.

--ViewModel

La carpeta viewmodel alberga los ViewModel responsables de manejar el estado de la interfaz y unir las operaciones entre la interfaz y la capa de datos.
--Localización

La app emplea los servicios de localización de Android para
determinar la ubicación del usuario y facilitar la búsqueda de lugares
cercanos.

-- Servicios externos

La app hace uso de Retrofit y OkHttp para realizar solicitudes
a servicios en línea.

Coil se utiliza para cargar imágenes.

--Almacenamiento local
-Room

Se emplea para administrar la información guardada
localmente y los destinos favoritos.

--DataStore

Es utilizado para gestionar las preferencias de la aplicación.
   
--Autorizaciones

La aplicación requiere permisos que se relacionan con:

Conexión a Internet.
Ubicación exacta.
Ubicación general.

--Especificaciones

Para llevar a cabo el proyecto se requiere:

Android Studio.
Android SDK.
JDK compatible con el proyecto.
Dispositivo Android o emulador.
Conexión a Internet para las características que dependen de
servicios externos.

--Implementación

Clonar el repositorio.
Abrir el proyecto en Android Studio.
Esperar la sincronización de Gradle.
Conectar un dispositivo Android o lanzar un emulador.
Iniciar la aplicación.

--APK y AAB

El proyecto permite crear:

APK: archivo que se puede instalar en dispositivos Android.
AAB: Android App Bundle firmado para su distribución.
DIAGRAMA DE ARQUITECTURA 
La aplicación AppTurismo utiliza una arquitectura basada en MVVM
 y el patrón Repository, separando la interfaz de usuario, 
 la lógica de presentación y las fuentes de datos.

![AppTurismo.drawio.diagrama.png](imagenes/AppTurismo.drawio.diagrama.png)
## Capturas de pantalla

### Pantalla principal

![Pantalla principal](imagenes/inicio.png)

### Lista de destinos y filtros 

![Lista de destinos](imagenes/destinos.png)

### Detalle del destino

![Detalle del destino](imagenes/detalle.png)

### Favoritos

![Favoritos](imagenes/favoritos.png)

### Ubicación, confirmacion y muestra de ubicacion

![Ubicación](imagenes/ubicacion.png)

--Proyecto académico

Nombre: AppTurismo

Tipo: Aplicación móvil Android

Lenguaje principal: Kotlin

Interfaz: Jetpack Compose

Arquitectura: MVVM

Base de datos: Room

Preferencias: DataStore

