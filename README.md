# Testing
--------
Unit Test
  - Selected fragments 
    

     Caso de prueba:
     
     ```kotlin
     private fun isAnyBlank(fields: List<String?>): Boolean{
         for(f in fields) {
            if(f.isNullOrBlank()) return true
         }
         return false
     }
     ```

     ```kotlin
     import org.junit.Assert.assertFalse
     import org.junit.Assert.assertTrue
     import org.junit.Test
     class testFields{
       @Test fun nullFields(){
           assertTrue(true, isAnyBlank())
       }
     }
     ```


     ```kotlin
     private fun validateFields(fields: List<String?>): Boolean {
        for (f in fields) {
            if (f.isNullOrBlank()) return false
        }
        return true
     }
     ```

Integration Test

  - Caso de prueba:

     Verificar si el usuario introduce (email,contraseña) correctas sera dirigido a el punto de entrada MainActivity.kt
     


     

         
