# JThemeManager

JThemeManager es una libreria que **se encarga de Aplicar UI 
 personalizada y una Paleta de colores minimalista** , esta se 
 puede aplicar de dos maneras segun el programador lo decida, jerarquica 
 y por tono de luminosidad. a base de archivos .properties.
  
***
### Theme By Tone

Este modo se aplica de forma mas manual y directa, bajo una regla igual 
 de sencilla una **escala de grises simplificada (negro, gris, 
 blanco),** segun el cual se aplicara el color primario secundario 
 y tersario del tema en cuestion, con el texto ocurre de una manera 
 similar, dando mucho control al diseñador sobre de que color es un 
 componente x. ejemplo:
![DarkThemeByTone.png](media/DarkThemeByTone.png)
 

***
### Theme By Level

Este modo se aplica de forma automatica bajo una regla sencilla: la **jerarquia** . 
 el tema al aplicarse desde el componente padre hasta los componentes 
 hijos, **va incrementando su nivel, aumentando la luminocidad o 
 oscuridad** del componente bajo cierto limite establecido de 3 
 niveles antes de reiniciar el tono al original. ejemplo:
 
![DarkThemeByLevel](media/DarkThemeByLevel.png)
 

por defecto se aplica el tema por nivel al ser el automatico y mas 
 sencillo, el por tono solo se activa agregando a SetTheme las palabras 
 "ByTone" (mas adelante se explicara) la ventana utilizada fue la 
 siguiente, cabe aclarar que la ventana utilizada fue la que se 
 proporciona dentro de la libreria de forma opcional, esta no se aplica 
 con el Thema sino de forma manual:


![noTheme](media/NoTheme.png)
 

## Como utilizar

esta se utiliza de forma sencilla, en la carpeta dist encontraras la 
 libreria esta incluye la ventana personalizada y el Theme Manager, 
 ademas de esto se incluye PathUtils para poder trabajar de forma 
 sencilla con los directorio externos. despues de integrar el jar a tu 
 proyecto tienes varias opciones utilizar un tema externo 
 (dir/theme.propertie) que tengas localmente, un propertie que tengas en 
 recursos o un propertie directamente desde codigo y por ultimo si quiere 
 utilizarlo sin mayor precupacion puedes elegir la opcion de cargar un 
 tema a base de un color este generara los otros 8 colores necesarios 
 para aplicar el tema el codigo se detallara por partes.

 ##### crear una instancia

```java
ThemeManager themeManager = ThemeManager.getInstance()
```
luego con ello aseguramos que si se llama en otro punto ThemeManager sea 
 el mismo, ahora podemos cargar los temas por distintos medios:
##### Temas por defecto o por properties en codigo:	
```java
//Temas por defecto o por properties en codigo
themeManager.loadThemeFromProperties(themeM.getDarkThemeDefault(), "nameTheme");
themeManager.loadThemeFromProperties(themeM.getLightThemeDefault(), "nameTheme2");

//tema por un color:
themeManager.loadThemeFromColor(Color.BLACK);
//tema desde recursos:
themeManager.loadThemeFromResource(resourcePath);
//tema desde directorio Externo:
themeManager.loadThemeFromFilePath(FilePath);
//tema desde directorio externo o recurso interno sino se encuentra:
themeM.loadThemeFromFileOrDefault(filePath, resourcePath);
```
serian todas las formas de cargar un tema, para aplicarlo tenemos muchas 
 formas, directamente a un contenedor o a la ventana :
```java
themeManager.setThemeByTone(window);
 themeManager.setTheme(window);
```
recordando que si se utiliza el tema por tono deberia inicializarlo 
 primero llamandolo para que asi guarde los valores de luminosidad 
 originales y no los valores despues de haber aplicado cualquier tema... 
 los componentes se registran la primera vez que llama a serThemeByTone.
si quiere aplicar el tema desde una ventana puede usar:
```java
themeManager.saveMainWindow(window);
//luego desde otra clase luego de haber cargado algun tema:
 theme.setThemeInMainWindow();
 theme.setThemeByToneInMainWindow();
```
##### uso de JWindowPlus

este es un componente personalizado extendido desde JFrame, con el 
 objetivo de ser lo mas modificable posible el codigo esta en este 
 repositorio para verlo detalladamente como usarlo:
##### creamos una instancia la cual necesita un panel donde tendras todo el 
 diseño de tu UI

```java
//creamos una instancia JavaWindowPlus window = new JavaWindowPlus(panel);
//luego podemos hacerla visible y centrarla:
 window.setVisiblePlusRelativeTo(null);
```
Esta ventan tiene funcionalidades como ocupar el tamaño preferido del 
 componente hijo, es decir el panel, tambien puedes modificar cosas como 
 el borde, title bar, agregar botones a este mismo title bar,
