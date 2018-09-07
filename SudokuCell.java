package sudoku;

import java.util.ArrayList;

/**Класс представляет клетку поля судоку. 
@author Артемьев Р.А.
@version 28.06.2018 */
public class SudokuCell 
{
   /**Количество возможных значений в клетке*/
   public final int NUMBER_OF_POSSIBLE_VALUES = 9; 
   /**Числовое значение в клетке*/
   private int value; 
   /**true - значение предполагаемое, false - точное*/
   private Boolean assumption; 
   /**Порядковый номер предположения*/
   private int numberAssumption;
   /**Список возможных значений в клетке*/
   private ArrayList<Integer> possibleValues; 
   
   /**Метод создаёт объект представляющий клетку*/
   public SudokuCell()
   {
	   value = 0;  
	   assumption = false;
	   numberAssumption = 0;
	   possibleValues = new ArrayList<Integer>(NUMBER_OF_POSSIBLE_VALUES);
	   for(int i = 0; i < NUMBER_OF_POSSIBLE_VALUES; i++)
	    {
	    	possibleValues.add(new Integer(i+1));
	    }
   }
   
   /**Переопределяем метод преобразовывающий экземпляр класса в строку*/
   @Override
   public String toString()
   {
	   String str = String.format("Class:%s [value = [%d] assumption = [%b] numberAssumption = [%d]  possibleValues = %s]",
			   this.getClass().getSimpleName(),  value, assumption, numberAssumption, possibleValues);
	   
	   return str;
   }
   
   /**Метод приводит клетку в исходное состояние*/
   public void resetCell()
   {
	   value = 0;
	   assumption = false;
	   numberAssumption = 0;
	   possibleValues = new ArrayList<Integer>(NUMBER_OF_POSSIBLE_VALUES);
	   for(int i = 0; i < NUMBER_OF_POSSIBLE_VALUES; i++)
	    {
	    	possibleValues.add(new Integer(i+1));
	    }
   }
   
   /**Метод возвращает значение в клетке
	 @return значение в клетке*/
   public int getValue()
   {
	   return value;
   }
   
   /**Метод устанавливает новое значение в клетку, делая его единственно возможным
	 @param newValue  новое значение в клетке*/
   public void setValue(int newValue)
   {
	   value = newValue;
	   possibleValues = new ArrayList<Integer>(NUMBER_OF_POSSIBLE_VALUES);
	   possibleValues.add(new Integer(value));
   }
   
   /**Метод устанавливает первое из возможных значений в клетку
     если таких нет, не делает ничего*/
   public void setFirstValue()
   {
	   if(possibleValues.size() > 0)
	   {
	       value = possibleValues.get(0);
	   }
   }
 
   /**Метод возвращает информацию, является ли значение в клетке предполагаемым
	 @return true - если значение в клетке предполагаемое
     false - нет*/
   public Boolean getAssumption()
   {
	   return assumption;
   }
   
   /**Метод устанавливает, является ли значение предполагаемым 
	 @param newAssumption новое значение поля assumption, true - если значение предполагаемое
     false - нет*/
   public void setAssumption(Boolean newAssumption)
   {
	   assumption = newAssumption;
   }
   
   /**Метод возвращает номер предположения
	 @return номер предположения*/
   public int getNumberAssumption()
   {
	   return numberAssumption;
   }
   
   /**Метод устанавливает номер предположения, в случае отрицательного значения, не делает ничего
	 @param newNumberAssumption номер предположения*/
   public void setNumberAssumption(int newNumberAssumption)
   {
	   if(newNumberAssumption >= 0)
	   {
	      numberAssumption = newNumberAssumption;
	   }
   }
   
   /**Метод возвращает количество возможных значений в клетке
	 @return количество возможных значений в клетке*/
   public int getNumberPossibleValues()
   {
	   return possibleValues.size();
   }
   
   /**Метод проверяет, есть ли число, указанное в качестве параметра
      в списке возможных значений клетки
     @param n значение на возможность которого будет проверяться клетка
	 @return true - если число есть в списке возможных значений клетки
	         false - если нет, или если клетка не пустая*/
 public Boolean testCell(int n)
 {
	 Boolean flag = false;
	 //Если клетка пустая
	 if(value == 0)
	 {
	    for(int m : possibleValues)
	    {
		   if(m == n)
		   {
			   flag = true;
		   }
	    }
	 }
	 return flag;
 }
 
   /**Метод удаляет одно из возможных значений в клетке
	 @param removeValue удаляемое значение
	 @return возвращает true если такое значение есть в списке возможных и метод его удалил
	 false - если такого значения нет, и он его не удалил */
   public Boolean removePossibleValue(int removeValue)
   {
	   if(possibleValues.remove(new Integer(removeValue)))
	   {
		   return true;
	   }
	   else 
	   {
		   return false;
	   }
   }
   
   /**Метод устанавливает единственное возможное значение в клетку.
      Если возможных значений больше чем одно, метод не сделает ничего*/
   public void setOnlyValue()
   {
	   if(possibleValues.size() == 1)
	   {
		   value = possibleValues.get(0); 
	   }
   }
   
}
   



