=======
# Testing
--------
Unit Test 
     
- Prueba Unitaria 01: Validación de Email

Fragmento Seleccionado:
     
```kotlin
public fun validateEmail(email: String): Boolean {
   val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
   return emailRegex.matches(email)
}
```

Caso de Prueba y Resultado:

```kotlin
class EmailSignUpViewModelTest{
@Test
fun EmailValidatorUnitTest(){
   var emailTestCase : String? = "example@gmail.com"
   assertEquals(true,EmailSignUpViewModel().validateEmail(emailTestCase!!))
}
```

- Explicación de la prueba:
 
Esta prueba valida si el email ingresado por el usuario es correcto según los parámetros seleccionados, es decir, debe contener caracteres como @. En este caso en particular se utilizó el email "example@gmail.com" y se validó correctamente.
  
- Prueba Unitaria 02: Validación de Contraseñas

 Fragmento Seleccionado:

```kotlin
public fun validatePasswords(p1: String, p2: String) = p1 == p2
```
     
Caso de Prueba y Resultado:
    
```kotlin
  @Test
  fun  PasswordConfirmator(){
     var pass : String? = "123"
     var confPass : String? = "123"
     assertEquals(true,EmailSignUpViewModel().validatePasswords(pass!!,confPass!!))
  }
```

- Explicación de la prueba:
    
Esta prueba valida si la contraseña que el usuario crea, en su doble confirmación, es correcta; es decir, si las dos contraseñas (Password y ConfirmPassword) son iguales. En este caso en particular, se observa que se valida correctamente.


- Prueba Unitaria 03: Validación de Campos

Fragmento Seleccionado:
    
```kotlin
private fun validateFields(fields: List<String?>): Boolean {
   for (f in fields) {
      if (f.isNullOrBlank()) return false
   }  
   return true
}
```

Caso de Prueba y Resultado:
      
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

- Explicación de la prueba:
    
Esta prueba valida si los campos que se deben llenar al momento del inicio de sesión son validados, es decir, aquellos campos que poseen restricciones de NOT NULL deben validarse para evitar errores.  
     
Integration Test

- Prueba de Integración 04: Integración con Firebase
  
  - Caso de prueba (Firebase integration):
    
     Ingresar el (Email,Password) validos
     Email = example@gmail.com
     Password = example1234

  - Salida esperada:

    Redireccion al punto de entrada MainActivity.kt
  
  - Explicación de la prueba:
 
    Esta prueba pretende validar y verificar si el servicio en la nube Firebase Auth está conectando correctamente con la aplicación. Al ingresar las credenciales, se espera que se validen en Auth Firebase y determinen si están activas, dando como resultado verdadero o falso. Al ingresar los datos del caso de prueba y hacer clic en "Ingresar", se está probando la integración de Firebase.
    
- Prueba de Integración 05: Interacción entre ViewModel y View

  - Caso de prueba (VieWModel - View):

   Ingresar (Email,password) Invalidos
   Email = example@gmail.com
   Password = example1

  - Salida esperada:
     
   ERROR Datos incorrectos o no se encuentra registrado

  - Explicación de la prueba:

  Esta prueba valida la interacción entre los módulos del ViewModel y View. Al ingresar los datos válidos/inválidos, el módulo View debe interactuar con el ViewModel para que este utilice el Modelo y valide los datos, además de comunicar con la View para que muestre un SnackBar en caso de que los datos sean incorrectos.

- Prueba de Integración 06: Registro de Nuevo Usuario
  
  - Caso de prueba (ViewModel - Model):
  
            Registro de nuevo usuario:
                 FirstName: Hello
                 LastName: World
                 UserName: HelloWorld
                 Email: hello@gmail.com
                 Password: hello123
    
  - Salida esperada:
  
    Se espera que el usuario se registre con éxito y sea redirigido al layout principal de la aplicación.
    
  - Explicación de la prueba:
  
    Esta prueba valida y verifica la interacción entre los módulos ViewModel y Model al registrar un nuevo usuario. Se espera que el ViewModel utilice el Model para verificar todos los campos proporcionados por el usuario, además de agregar la información a la base de datos. El objetivo es asegurarse de que el registro sea exitoso y que el usuario sea dirigido correctamente al layout principal de la aplicación.

    
- Prueba de Integración 07: Adaptación de Layouts al Modo Oscuro/Claro

  - Caso de prueba (Black Box):

  Cambiar configuracion del sistema (modo oscuro y modo claro)

  - Salida esperada:

  Correcta Adaptacion de los layouts de acuerdo a la configuracion del sistema

  - Explicación de la prueba:

  Esta prueba pretende validar y verificar si los widgets que están en la aplicación se adaptan según el modo (oscuro, claro) utilizado en el sistema operativo. Se realiza cambiando el modo o tema del sistema y observando cómo cambian los layouts de la aplicación.

  - Prueba de Integración 08: Base de Datos / Firebase
  
  - Caso de prueba (Base de datos - Firebase):
  
         Registro de usuario:
              Email: hello10@gmail.com
              Password: hello123
    
         Inicio de sesión con credenciales incorrectas:
              Email: hello123@gmail.com
              Password: hello10
    
  - Salida esperada:
  
    Se espera que al intentar iniciar sesión con credenciales incorrectas, se lance un error indicando que las credenciales son incorrectas. Además, al registrar el usuario, se espera que la creación en la base de datos Firebase sea exitosa.
    
  - Explicación de la prueba:
  
    Se sospecha que hay un error en la creación del usuario en la base de datos Firebase, ya que al intentar iniciar sesión con credenciales incorrectas (intercambiadas), se inicia sesión brevemente antes de cerrarse automáticamente. Esto sugiere un problema en la autenticación de Firebase o en la creación del usuario en la base de datos.
    
- Prueba de Integración 09: Interfaz de Usuario
  
  - Caso de prueba (Interfaz de usuario):
  
    Ocultar la barra superior al hacer scroll en la pantalla de inicio o perfil.
    
  - Salida esperada:
  
    Se espera que al hacer scroll en la pantalla de inicio o perfil, la barra superior se oculte para mejorar la experiencia del usuario.
    
  - Explicación de la prueba:
  
    Actualmente, al hacer scroll en la pantalla de inicio, la barra superior desaparece correctamente. Sin embargo, al hacer scroll en la pantalla de perfil, la barra superior no se oculta correctamente y permanece bloqueada, lo que afecta la experiencia del usuario. Esta prueba tiene como objetivo verificar y corregir este comportamiento incorrecto.

- Prueba de Integración 10: Interfaz de Usuario
  
  - Caso de prueba (Interfaz de usuario):
  
    Ocultar la barra superior al hacer scroll en la pantalla de inicio o perfil.
    
  - Salida esperada:
  
    Se espera que al hacer scroll en la pantalla de inicio o perfil, la barra superior se oculte para mejorar la experiencia del usuario.
    
  - Explicación de la prueba:
  
    En ocasiones, la barra superior se ocultaba sin motivo aparente, lo que afectaba la experiencia del usuario.

- Prueba de Integración 11: Interfaz de Usuario
  
  - Caso de prueba (Interfaz de usuario):
  
    Funcionamiento del modo oscuro.
    
  - Salida esperada:
  
    Se espera que la aplicación funcione correctamente en modo oscuro y que se inicie sin problemas incluso si el teléfono está en modo oscuro.
    
  - Explicación de la prueba:
  
    Anteriormente, el modo oscuro no funcionaba correctamente y la aplicación no se iniciaba si el teléfono estaba en modo oscuro.
        
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
|08|INTEGRACION|BASE DE DATOS - FIREBASE|Registro de usuario: Email: hello10@gmail.com ; Password: hello123. Inicio de sesion con credenciales incorrectas: Email: hello123@gmail.com ; Password: hello10|INCORRECTO|
|09|INTERFAZ DE USUARIO|TODA LA APLICACION (SCROLL)|Ocultar la barra superior al hacer scroll en la pantalla de inicio o perfil|INCORRECTO|
|10|INTERFAZ DE USUARIO|TODA LA APLICACION (SCROLL)|Ocultar la barra superior al hacer scroll en la pantalla de inicio o perfil|CORRECTO|
|11|INTERFAZ DE USUARIO|TODA LA APLICACION (MODO OSCURO)|Funcionamiento del modo oscuro|CORRECTO|
