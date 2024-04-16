# Testing
--------
Unit Test
  - Selected fragments 
    

     ```kotlin
     private fun isAnyBlank(fields: List<String?>): Boolean{
         for(f in fields) {
            if(f.isNullOrBlank()) return true
         }
         return false
     }
     ```

     ```kotlin
import org.junit.Test;
     import static org.junit.Assert.assertFalse;
     import static org.junit.Assert.assertTrue;
     ```

     

         
