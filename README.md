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

     

         
