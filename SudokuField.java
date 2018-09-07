package sudoku;

import javax.swing.table.AbstractTableModel;

/**Класс представляет поле судоку в целом. 
@author Артемьев Р.А.
@version 29.06.2018 */
public class SudokuField 
{
	   /**Матрица ячеек поля судоку*/
	   SudokuCell[][] field;
	   /**Количество строк в поле судоку*/
	   private  int numRow;  
	   /**Количество столбцов в поле судоку*/
	   private  int numColumn; 
	   /**Количество сделанных предположений*/
	   private int quantity_assumption; 
	   
	   /**Метод создаёт объект представляющий поле судоку
	     @param model  имя модели таблицы, по которой строится объект*/
	   public SudokuField(AbstractTableModel model)
	   {
		   numRow = model.getRowCount();
		   numColumn = model.getColumnCount();
		   quantity_assumption = 0;
		   field = new SudokuCell[numRow][numColumn];
		   for(int i = 0; i < numRow; i++)
		    {
		  	   for(int j = 0; j < numColumn; j++)
		  	   {
		  		  field[i][j] = new SudokuCell();
		  		  if(model.getValueAt(i, j) == null)
		  		  {}
		  		  else
		  		  {
		  			 field[i][j].setValue(Integer.parseInt((String)model.getValueAt(i, j)));
		  		  }
		  	   }
		    }
	   }
	   
	   /**Метод возвращает объект представляющий ячейку по номеру её строки и столбца.
	    @param row  номер строки
	    @param column номер столбца
	    @return объект представляющий ячейку.*/
	    public SudokuCell getCell(int row, int column) 
	    {
	        return field[row][column];
	    }
	   /**Метод выводит на экран содержание поля судоку*/ 
	   public void printSudokuField()
	   {
	   	for(int i = 0; i < numRow; i++)
	       {
	       	for(int j = 0; j < numColumn; j++)
	       	{
	       		if(field[i][j].getNumberAssumption() > 0)
	       		{
	       			if(field[i][j].getAssumption())
	       			{ 
	       				System.out.print(field[i][j].getValue() + "] ");
	       			}
	       			else
	       			{
	       			    System.out.print(field[i][j].getValue() + ") ");
	       			}
	       		}
	       		else
	       		{	
	       		   System.out.print(field[i][j].getValue() + "  ");
	       		}
	       		if((j == 2) || (j == 5))
	       		{
	       			System.out.print(" ");
	       		}
	       		if(((j == 8) && (i == 2)) || ((j == 8) && (i == 5)) || ((j == 8) && (i == 8)))
	       		{
	       			System.out.println();
	       		}
	           }
	       	System.out.println();
	       }
	   }
	 /**Метод находит клетку поля с наименьшим количеством возможных значений
	 и выставляет в неё предположительное значение. 
	 @return true - если метод устанавил предположительное значение
	 false - если нет, поле заполнено */ 
	 public Boolean doAssumption()
	 {
	 	Boolean yes = false;
	 	int min = 10;
	 	int line = 0;
	 	int column = 0;
	     for(int i = 0; i < numRow; i++)
	     {
	     	for(int j = 0; j < numColumn; j++)
	         { 
	     		if(field[i][j].getValue() == 0)
	     		{
	     		   if(field[i][j].getNumberPossibleValues() < min)
	     		   {
	     			   min = field[i][j].getNumberPossibleValues();
	     			   line = i;
	     			   column = j;
	     		   }
	             }
	         }
	     }
	     if(min < 10)
	     {
	    	quantity_assumption++;   	
	     	field[line][column].setAssumption(true);
	     	field[line][column].setNumberAssumption(quantity_assumption);
	     	yes = true;
	     	field[line][column].setFirstValue();
	     }
	     return yes;
	 }
	 /**Метод проверяет все клетки поля, убирая лишние возможные значения, 
	  если в клетке одно возможное значение, оно становится действующим  
	 @return true - если метод устанавил одно значение
	 false - если нет */
	 public Boolean cellSearch()
	 {
	 	for(int i = 0; i < numRow; i++)
	     {
	     	for(int j = 0; j < numColumn; j++)
	     	{
	     		if(field[i][j].getValue() == 0)
	     		{
	     			/*Проходим по строке*/
	     			for(int k = 0; k < numColumn; k++)
	     			{
	     				if(field[i][k].getValue() != 0)
	     				{
	     					Boolean f = field[i][j].removePossibleValue(field[i][k].getValue());
	     					if(f)
	     					{
	     					    if(field[i][k].getNumberAssumption() > 0)
	     					    {
	     						   field[i][j].setNumberAssumption(quantity_assumption);
	     					    }    					  			
	     				        if(field[i][j].getNumberPossibleValues() == 1)
	     				        {   					      
	     					       field[i][j].setOnlyValue();	
	     					       return true;
	     				        }	
	     				   }
	     			   }
	     			}
	     			/*Проходим по столбцу*/
	     			for(int k = 0; k < numRow; k++)
	     			{   			
	         				if(field[k][j].getValue() != 0)
	         				{
	         					Boolean f = field[i][j].removePossibleValue(field[k][j].getValue());
	         					if(f)
	         					{
	         					    if(field[k][j].getNumberAssumption() > 0)
	         					    {
	         						   field[i][j].setNumberAssumption(quantity_assumption);
	         					    }    					  			
	         				        if(field[i][j].getNumberPossibleValues() == 1)
	         				        {      					      
	         					       field[i][j].setOnlyValue();	
	         					       return true;
	         				        }	
	         				   }
	         				}
	     			}
	     			/*Проходим по сектору*/
	     			//Если клетка находится в 1-м секторе
	     			if(((i >= 0) && (i <= 2)) && ((j >= 0) && (j <= 2)))
	     			{
	     				for(int n = 0; n < 3; n++)
	     				{
	     					for(int m = 0; m < 3; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					        return true;										
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 2-м секторе
	     			if(((i >= 0) && (i <= 2)) && ((j >= 3) && (j <= 5)))
	     			{
	     				for(int n = 0; n < 3; n++)
	     				{
	     					for(int m = 3; m < 6; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;									
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 3-м секторе
	     			if(((i >= 0) && (i <= 2)) && ((j >= 6) && (j <= 8)))
	     			{
	     				for(int n = 0; n < 3; n++)
	     				{
	     					for(int m = 6; m < 9; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;										
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 4-м секторе
	     			if(((i >= 3) && (i <= 5)) && ((j >= 0) && (j <= 2)))
	     			{
	     				for(int n = 3; n < 6; n++)
	     				{
	     					for(int m = 0; m < 3; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;									
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 5-м секторе
	     			if(((i >= 3) && (i <= 5)) && ((j >= 3) && (j <= 5)))
	     			{
	     				for(int n = 3; n < 6; n++)
	     				{
	     					for(int m = 3; m < 6; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;									
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 6-м секторе
	     			if(((i >= 3) && (i <= 5)) && ((j >= 6) && (j <= 8)))
	     			{
	     				for(int n = 3; n < 6; n++)
	     				{
	     					for(int m = 6; m < 9; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;										
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 7-м секторе
	     			if(((i >= 6) && (i <= 8)) && ((j >= 0) && (j <= 2)))
	     			{
	     				for(int n = 6; n < 9; n++)
	     				{
	     					for(int m = 0; m < 3; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;									
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 8-м секторе
	     			if(((i >= 6) && (i <= 8)) && ((j >= 3) && (j <= 5)))
	     			{
	     				for(int n = 6; n < 9; n++)
	     				{
	     					for(int m = 3; m < 6; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;										
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}
	     			//Если клетка находится в 9-м секторе
	     			if(((i >= 6) && (i <= 8)) && ((j >= 6) && (j <= 8)))
	     			{
	     				for(int n = 6; n < 9; n++)
	     				{
	     					for(int m = 6; m < 9; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	             					Boolean f = field[i][j].removePossibleValue(field[n][m].getValue());
	             					if(f)
	             					{
	             					    if(field[n][m].getNumberAssumption() > 0)
	             					    {
	             						   field[i][j].setNumberAssumption(quantity_assumption);
	             					    }    					  			
	             				        if(field[i][j].getNumberPossibleValues() == 1)
	             				        {
	             				        	field[i][j].setOnlyValue();	
	              					       return true;										
	             				        }	
	             				   }
	             				}
	     					}
	     				}
	     			}		
	     		}
	     	}	
	     }
	 	return false;
	 	
	 }

	 /**Метод проверяет в скольких клетках каждой строки, столбца 
	  и сектора может стоять каждое из значений, и если 
	  только в одной - выставляет это значение
	 @return true - если метод устанавил одно значение
	 false - если нет */
	 public Boolean searchByNumbers()
	 {
	 	for(int number = 1; number < 10; number++)
	 	{
	 		//Проходим по строкам
	 		for(int i = 0; i < numRow; i++)
	 		{
	 			int count = 0;
	 			int column = 0;
	 			Boolean isAssumption = false;
	 			for(int j = 0; j < numColumn; j++)
	 			{	
	 				   if(field[i][j].getNumberAssumption() > 0)
	 				   {
	 					   isAssumption = true;
	 				   }
	 				   if(field[i][j].testCell(number))
	 				   {
	 					  count++;
	 					  column = j;
	 				   }	     	
	 			}
	 			if(count == 1)
	 			{			
	 				field[i][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[i][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 		}
	 		//Проходим по столбцам
	 		for(int j = 0; j < numColumn; j++)
	 		{
	 			int count = 0;
	 			int line = 0;
	 			Boolean isAssumption = false;
	 			for(int i = 0; i < numRow; i++)
	 			{	
	 				   if(field[i][j].getNumberAssumption() > 0)
	 				   {
	 					   isAssumption = true;
	 				   }
	 				   if(field[i][j].testCell(number))
	 				   {
	 					  count++;
	 					  line = i;
	 				   }	     	
	 			}
	 			if(count == 1)
	 			{				
	 				field[line][j].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][j].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 		}
	 		
	 		//Проходим по секторам
	 		//1-й сектор
	 		    int count = 0;
	 		    int line = 0;
	 		    int column = 0;
	 		    Boolean isAssumption = false;
	 		    for(int n = 0; n < 3; n++)
	 			{
	 				for(int m = 0; m < 3; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{			
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//2-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 0; n < 3; n++)
	 			{
	 				for(int m = 3; m < 6; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{				
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//3-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 0; n < 3; n++)
	 			{
	 				for(int m = 6; m < 9; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{			
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//4-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 3; n < 6; n++)
	 			{
	 				for(int m = 0; m < 3; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{			
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//5-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 3; n < 6; n++)
	 			{
	 				for(int m = 3; m < 6; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{			
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//6-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 3; n < 6; n++)
	 			{
	 				for(int m = 6; m < 9; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//7-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 6; n < 9; n++)
	 			{
	 				for(int m = 0; m < 3; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{	
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//8-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 6; n < 9; n++)
	 			{
	 				for(int m = 3; m < 6; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}
	 			//9-й сектор
	 		    count = 0;
	 		    line = 0;
	 		    column = 0;
	 		    isAssumption = false;
	 		    for(int n = 6; n < 9; n++)
	 			{
	 				for(int m = 6; m < 9; m++)
	 				{
	 					if(field[n][m].getValue() == 0)
	     				{
	 						if(field[n][m].getNumberAssumption() > 0)
	 						{
	 							isAssumption = true;
	 						}
	 						if(field[n][m].testCell(number))
	 						{
	 						   count++;
	 						   line = n;
	 						   column = m;
	 						}	
	     				}
	     				
	 				}
	 			}
	 			if(count == 1)
	 			{			
	 				field[line][column].setValue(number);
	 				if(isAssumption)
	 				{
	 					field[line][column].setNumberAssumption(quantity_assumption);
	 				}	
	 				return true;
	 			}	
	 	}
	 	 return false;
	 }

	 /**Метод проверяет все строки,столбцы и сектора поля на наличие повторных значений.
	 @return true - если повторные значения найдены
	 false - если нет */
	 public  Boolean searchMistake()
	 {
	 	int num = 0;
	 	for(int i = 0; i < numRow; i++)
	     {
	     	for(int j = 0; j < numColumn; j++)
	     	{
	     		if(field[i][j].getValue() != 0)
	     		{
	     			/*Проходим по строке*/
	     			for(int k = 0; k < numColumn; k++)
	     			{
	     				if(field[i][k].getValue() != 0)
	     				{
	     					if(field[i][k].getValue() == field[i][j].getValue())
	     					{
	     						num++;	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			/*Проходим по столбцу*/
	     			for(int k = 0; k < numRow; k++)
	     			{   			
	         				if(field[k][j].getValue() != 0)
	         				{
	         					if(field[k][j].getValue() == field[i][j].getValue())
	         					{    						
	         						num++;
	         					}
	         				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			/*Проходим по сектору*/
	     			//Если клетка находится в 1-м секторе
	     			if(((i >= 0) && (i <= 2)) && ((j >= 0) && (j <= 2)))
	     			{
	     				for(int n = 0; n < 3; n++)
	     				{
	     					for(int m = 0; m < 3; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{   								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 2-м секторе
	     			if(((i >= 0) && (i <= 2)) && ((j >= 3) && (j <= 5)))
	     			{
	     				for(int n = 0; n < 3; n++)
	     				{
	     					for(int m = 3; m < 6; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{  								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 3-м секторе
	     			if(((i >= 0) && (i <= 2)) && ((j >= 6) && (j <= 8)))
	     			{
	     				for(int n = 0; n < 3; n++)
	     				{
	     					for(int m = 6; m < 9; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{   								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 4-м секторе
	     			if(((i >= 3) && (i <= 5)) && ((j >= 0) && (j <= 2)))
	     			{
	     				for(int n = 3; n < 6; n++)
	     				{
	     					for(int m = 0; m < 3; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{  								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}  		
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 5-м секторе
	     			if(((i >= 3) && (i <= 5)) && ((j >= 3) && (j <= 5)))
	     			{
	     				for(int n = 3; n < 6; n++)
	     				{
	     					for(int m = 3; m < 6; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 6-м секторе
	     			if(((i >= 3) && (i <= 5)) && ((j >= 6) && (j <= 8)))
	     			{
	     				for(int n = 3; n < 6; n++)
	     				{
	     					for(int m = 6; m < 9; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{  							
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 7-м секторе
	     			if(((i >= 6) && (i <= 8)) && ((j >= 0) && (j <= 2)))
	     			{
	     				for(int n = 6; n < 9; n++)
	     				{
	     					for(int m = 0; m < 3; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{  								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 8-м секторе
	     			if(((i >= 6) && (i <= 8)) && ((j >= 3) && (j <= 5)))
	     			{
	     				for(int n = 6; n < 9; n++)
	     				{
	     					for(int m = 3; m < 6; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{   								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}
	     			if(num > 1)
	     			{
	     				return true;
	     			}
	     			num = 0;
	     			//Если клетка находится в 9-м секторе
	     			if(((i >= 6) && (i <= 8)) && ((j >= 6) && (j <= 8)))
	     			{
	     				for(int n = 6; n < 9; n++)
	     				{
	     					for(int m = 6; m < 9; m++)
	     					{
	     						if(field[n][m].getValue() != 0)
	             				{
	     							if(field[n][m].getValue() == field[i][j].getValue())
	             					{    								
	     	    						num++;
	             					}									
	             				}	
	     					}
	     				}
	     			}	
	     			if(num > 1)
	     			{
	     				return true;
	     			}  
	     			num = 0;
	     		}
	     	}	
	     }
	 	return false;
	 }

	 /**Метод меняет ошибочное предположение на другое, если на данном
	   уровне предположения это не возможно, переходит на уровень назад.
	 @return true - если предположение изменено.
	 false - если нет, достигнут нулевой уровень предположения. */
	 public Boolean correctMistake()
	 {
	 	Boolean b = false;
	 	for(int i = 0; i < numRow; i++)
	     {
	     	for(int j = 0; j < numColumn; j++)
	     	{
	     	  if(field[i][j].getNumberAssumption() >= quantity_assumption)
	     	  {
	     		if(field[i][j].getAssumption())
	     		{
	     			field[i][j].removePossibleValue(field[i][j].getValue());
	     			if(field[i][j].getNumberPossibleValues() > 0)
	     			{
	     				field[i][j].setFirstValue();
	     			}
	     			else
	     			{
	     				if(quantity_assumption == 0)
	     				{
	     					System.exit(0);
	     				}
	     				else
	     				{
	     					field[i][j].setAssumption(false); 
	     					b = true;
	     				}
	     			}
	     		}
	     		else
	     		{	
	     				field[i][j].resetCell();
	     		}	
	     	}
	       }
	     }
	 	if(b)
	 	{
	 		quantity_assumption--;
	 	}
	 	return b;
	 }
	 
	 /**Метод проверяет, заполнено ли поле
	 @return true - если поле заполено
	 false - если нет*/
	 public Boolean isFull()
	 {
	 	Boolean flag = true;
	 	for(int i = 0; i < numRow; i++)
	     {
	   	   for(int j = 0; j < numColumn; j++)
	   	   {
	   		  if(field[i][j].getValue() == 0)
	   		  {
	   			flag = false;
	   		  }
	   	   }
	     }
	 	return flag;
	 }

}
