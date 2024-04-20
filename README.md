# Testing
--------
Unit Test 
     
- Prueba 01

Fragmento Seleccionado
     
```kotlin
public fun validateEmail(email: String): Boolean {
   val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
   return emailRegex.matches(email)
}
```

Prueba Unitaria:

```kotlin
class EmailSignUpViewModelTest{
@Test
fun EmailValidatorUnitTest(){
   var emailTestCase : String? = "example@gmail.com"
   assertEquals(true,EmailSignUpViewModel().validateEmail(emailTestCase!!))
}
```
- Explicacion:
 
Esta prueba valida si el email ingresado por el usuario es correcto de acuerdo a los parametros seleccionados, es decir debe contener caracteres como @, en este caso en particular se hizo uso del email 
example@gmail.com (Valido correctamente)

  
- Prueba 02

 Fragmento Seleccionado

```kotlin
public fun validatePasswords(p1: String, p2: String) = p1 == p2
```

     
Prueba Unitaria:
    
```kotlin
  @Test
  fun  PasswordConfirmator(){
     var pass : String? = "123"
     var confPass : String? = "123"
     assertEquals(true,EmailSignUpViewModel().validatePasswords(pass!!,confPass!!))
  }
```
- Esxplicacion:
    
Esta prueba valida si la contraseña que crea el usuario, en su doble confirmacion es correcta, es decir si las dos contraseñas (Password y ConfirmPassword) son iguales, en este caso en particular se observa que se valida correctamente


- Prueba 03

Fragmento Seleccionado
    
```kotlin
private fun validateFields(fields: List<String?>): Boolean {
   for (f in fields) {
      if (f.isNullOrBlank()) return false
   }  
   return true
}
```


Prueba Unitaria;
      
```kotlin
import org.junit.Test
import org.junit.jupiter.api.Assertions.*


Prueba Unitaria
class EmailSignInViewModelTest{
  @Test
  fun validateFields_Check(){
    var email : String? = "email@gmail.com" 
    var password : String? = "email12345"
    val fieldsOfTestCase = listOf(email,password)
    assertEquals(true,EmailSignInViewModel().validateFields(fieldsOfTestCase))
  }
}
```

- Explicacion:
    
Esta prueba valida si los campos que se deben llenar al momento del loggin son validados, es decir aquellos campos que poseen constraints de NOT NULL deberan validarse para evitar errores
     
     
Integration Test

- Prueba 04
  
  (Firebase integration)
  - Caso de prueba:
    
     Ingresar el (Email,Password) validos
     Email = example@gmail.com
     Password = example1234

  - Salida esperada

    Redireccion al punto de entrada MainActivity.kt
  
  - Explicacion
 
    Esta prueba pretende validar y verificar si el servicio en la nube Firebase Auth esta conectando correctamente con la aplicacion, al ingresar las credenciales se espera que se valida sobre el Auth Firebase y determine si estas estan activas dando como resultado verdadero o no (dando como resultado falso), asi entonces al ingresar los datos del caso de prueba y dar click en ingresar se esta probando la integracion de firebase (inmediatanmente da el click de ingresar)
    
- Prueba 05
  
   (VieWModel - View)
  
  - Caso de prueba:

   Ingresar (Email,password) Invalidos
   Email = example@gmail.com
   Password = example1

  - Salida Esperada
     
   ERROR Datos incorrectos o no se encuentra registrado

  - Explicaicon

  Esta prueba valida la interaccion entre los modulos del ViewModel y View a partir de el caso de prueba, especificamente al ingresar los datos VALIDOS/INVALIDOS el modulo View tiene que interactuar con el ViewModel para que este utilize el Modelo y valide los datos, ademas de comunicar con la View para que muestra un SnackBar (pantalla emergente) en caso de que los datos sean incorrectos.

- Prueba 06

 (ViewModel - Model)

- Caso de prueba
FirstName: Hello
LastName: World
UserName: HelloWorld
Email: hello@gmail.com
Password: hello123

- Salida Esperada
    
Paso al layout principal de manera satisfactoria

- Explicacion de la prueba:
    
  Esta prueba pretende validar y verificar la interaccion entre los modulo ViewModel y View, a partir de el registro de un nuevo usuario, ya que al realizar dicha accion el modulo ViewModel usa al Model para poder verificar todos los campos, ademas de agregar a la base de datos
    
- Prueba 07

  - Caso de prueba

  Cambiar configuracion del sistema (modo oscuro y modo claro)

  - Salida Esperada

  Correcta Adaptacion de los layouts de acuerdo a la configuracion del sistema

  - Explicacion

  Esta prueba pretende validar y verificar si los widgets que estan en la aplicacion se adaptan segun el MODO (OSCURO,CLARO) utilizado en el Sistema operativo, lo realiza cambiando el modo o tema del sistema y observando como cambia los layouts de la aplicacion

  - Prueba 08

  (Base de datos - Firebase)
  
  - Caso de prueba:
  
    Registro de usuario:
    Email: hello10@gmail.com
    Password: hello123
    
    Inicio de sesión con credenciales incorrectas:
    Email: hello123@gmail.com
    Password: hello10
    
  - Salida Esperada:
  
    Se espera que al intentar iniciar sesión con credenciales incorrectas, se lance un error indicando que las credenciales son incorrectas. Además, al registrar el usuario, se espera que la creación en la base de datos Firebase sea exitosa.
    
  - Explicación de la prueba:
  
    Se sospecha que hay un error en la creación del usuario en la base de datos Firebase, ya que al intentar iniciar sesión con credenciales incorrectas (intercambiadas), se inicia sesión brevemente antes de cerrarse automáticamente. Esto sugiere un problema en la autenticación de Firebase o en la creación del usuario en la base de datos.
    
- Prueba 09

  (Interfaz de usuario)
  
  - Caso de prueba:
  
    Ocultar la barra superior al hacer scroll en la pantalla de inicio o perfil.
    
  - Salida Esperada:
  
    Se espera que al hacer scroll en la pantalla de inicio o perfil, la barra superior se oculte para mejorar la experiencia del usuario.
    
  - Explicación de la prueba:
  
    Actualmente, al hacer scroll en la pantalla de inicio, la barra superior desaparece correctamente. Sin embargo, al hacer scroll en la pantalla de perfil, la barra superior no se oculta correctamente y permanece bloqueada, lo que afecta la experiencia del usuario. Esta prueba tiene como objetivo verificar y corregir este comportamiento incorrecto.
        
Resumen

|ID|TIPOPRUEBA|ENTORNOPRUEBA|CASOPRUEBA|CORRECTA/INCORRECTA|
|-|-|-|-|-|
|01|UNITARIA |INGRESO DE EMAIL SOBRE EL LOGGIN |EMAIL: example@gmail.com | CORRECTO|
|02|UNITARIA|INGRESO DE PASSWORD AL REGISTRARSE, CONFIRMACION DE CONTRASEÑA|PASSWORD:123 ; PASSWORD:123|CORRECTO|
|03|UNITARIA|INGRESO DE EMAIL Y PASSWORD SOBRE EL LOGGIN|EMAIL: example@gmail.comk ; PASSWORD: email12345|CORRECTO|
|04|INTEGRACION|INTEGRACION DE FIREBASE AUTH (LOGIN)|EMAIL:example@gmail.com ; PASSWORD: example1234|CORRECTO|
|05|INTEGRACION|INTEGRACION DE LOS MODULOS ViewModel con View (LOGIN)|EMAIL: example@gmail.com : PASSWORD: exampl11234|CORRECTO|
|06|INTEGRACION|INTEGRACION DE LOS MODULOS ViewModel con Model (REGISTRO)|FirstName: Hello ; LastName: World ; UserWorld: HelloWorld ; Email: hello@gmail.com ; Password: hello123|Correcto|
|07|Black Box|Toda la aplicacion (frames)| Cambiar modo o tema del sistema a oscuro o claro|INCORRECTO|
