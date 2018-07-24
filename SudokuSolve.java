package sudoku;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;


/**Класс служит для решения головоломок судоку.
@author Артемьев Р.А.
@version 28.06.2018 */
public class SudokuSolve 
{
	public static void main(String[] args) 
	{
	    //Создаём фрейм и делаем его видимым
	    EventQueue.invokeLater(()->
        {
        	JFrame myframe = new SudokuFrame();
	    	myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	myframe.setTitle("Решить судоку");
	    	myframe.setResizable(false);
	    	myframe.setVisible(true);
		});
	}
}
/**Класс представляющий фрейм.*/
class SudokuFrame extends JFrame
{	
	/**Размер ячейки поля судоку в пикселях.*/
	public static final int SIZE_CELL = 40;
	/**Минимальное количесво известных значений в поле судоку.*/
	public static final int MIN_NUMBER_VALUE = 17;
	
	public SudokuFrame()
	{
		   //Задаём местоположение фрейма, добавляем в него элементы
		   Toolkit kit = Toolkit.getDefaultToolkit();
		   Dimension screenSize = kit.getScreenSize();
		   int screenHeight = screenSize.height;
		   setLocation(screenHeight / 3, screenHeight / 3);
		   SudokuTableModel model = new SudokuTableModel();
		   JTable tab = new SudokuTable(model);
		   tab.setDefaultRenderer(Object.class, new SudokuTableRenderer());
		   //Изменяем внешний вид таблицы
		   tab.setRowHeight(SIZE_CELL); 
		   TableColumnModel columnMod = tab.getColumnModel();
		   TableColumn col;
		   for(int i = 0; i < SudokuTableModel.TABLE_SIZE; i++)
		   {
		      col = columnMod.getColumn(i);
		      col.setPreferredWidth(SIZE_CELL);
		   }
		   tab.setCellSelectionEnabled(true);
		   //Добавляем таблицу и кнопки на фрейм
		   JPanel tablePanel = new JPanel();
		   tablePanel.add(tab);
		   //Меняем шрифт и выравнивание в ячейках таблицы
		   tab.setFont(new Font("SansSerif", Font.BOLD, 24));
		   DefaultTableCellRenderer r = (DefaultTableCellRenderer)tab.getDefaultRenderer(String.class);
		   r.setHorizontalAlignment(JLabel.CENTER);
		   r.setVerticalAlignment(JLabel.TOP);
		   JButton buttonStart = new JButton("Решить");
		   //Регистрируем обработчик для событий кнопки buttonStart
		   buttonStart.addActionListener(event ->
		   {
			   int v = 1;
			   int num = 0;
			   //Проверяем введённые значения на корректность
			   for(int i = 0; i < SudokuTableModel.TABLE_SIZE; i++)
		    	{
		    		for(int j = 0; j < SudokuTableModel.TABLE_SIZE; j++)
		    		{   
		    			if(model.getValueAt(i, j) != null)
		    			{
		    				if(model.getValueAt(i, j).length() == 0)
			    			{
			    				model.setValueAt(null, i, j);			    		
			    			}
		    				else
		    				{	
		    			       num++;
		    			       try
		    			       {
		    			          v = Integer.parseInt(model.getValueAt(i, j));
		    			       }
		    			       catch(NumberFormatException e)
		    			       {
		    				      JOptionPane.showMessageDialog(null, "Неккоректный ввод, нужно вводить числа от 1 до 9, а не символы");
		    				      return;
		    			       }
		    			       if((v <= 0)||(v > SudokuTableModel.TABLE_SIZE))
		    			       {
		    				       JOptionPane.showMessageDialog(null, "Неккоректный ввод, нужно вводить числа от 1 до 9");
		    				       return;
		    			       }
		    			    }
		    			}
		    		}
		    	}
			   if(num < MIN_NUMBER_VALUE)
			   {
				   JOptionPane.showMessageDialog(null, "Недостаточное количество известных значений, "
				   		+ "их должно быть минимум " + MIN_NUMBER_VALUE);
				   return;
			   }   
			   //На основе модели создаём объект представляющий поле судоку целиком
			   SudokuField field = new SudokuField(model);
			   if(field.searchMistake())
			   {
				   JOptionPane.showMessageDialog(null, "Неккоректный ввод, не должно быть "
				   	+ "повторных значений в каждой из строк, столбцов или секторов");
					 return;
			   }
			   field.printSudokuField();
			   System.out.println("-----------------------------------------");
			   /*Удаляем лишние возможные значения из клеток и проверяем в скольких
			     клетках каждой строки, столбца и сектора может стоять каждая из 
			     цифр, и если только в одной - выставляет эту цифру*/
			    do
			    {
				     while(field.cellSearch())
				     {}
			    }
				while(field.searchByNumbers());
			    /*Если судоку ещё не решено и в нём нет ошибок, делаем предположение
			      и повторяем предыдущую процедуру, если ошибка(ки) есть, исправляем их
			      меняя предположение или возвращаясь на уровень предположения назад*/
				while(!(field.isFull() && (!field.searchMistake())))
				{
					field.doAssumption();
				    do
				    {
					    while(field.cellSearch())
					    {}
				    }
					while(field.searchByNumbers());
				    while(field.searchMistake())
				   {  
					   while(field.correctMistake())
					   {}
			       }
				} 
				System.out.println("Судоку решено!");
				System.out.println("-----------------------------------------");
				if(!field.searchMistake())
				{
				   System.out.println("Ошибок нет!");
				}
				System.out.println("-----------------------------------------");
				//Выводим результат на консоль
				field.printSudokuField(); 
				//Выводим результат в модель таблицы
				model.fill(field);
				//Перерисовываем таблицу
				tab.repaint();
		   });
		   JButton buttonClear = new JButton("Очистить");
		   //Регистрируем обработчик для событий кнопки  buttonClear
		   buttonClear.addActionListener(event ->
		   {
			   model.clearModel();
			   tab.repaint();
		   });
		   JPanel buttonPanel = new JPanel();
		   buttonPanel.add(buttonStart);
		   buttonPanel.add(buttonClear);
		   add(tablePanel, BorderLayout.CENTER);
		   add(buttonPanel, BorderLayout.SOUTH);
	
		   pack();
	}
}

/**Класс представляющий модель таблицы.*/
class SudokuTableModel extends AbstractTableModel 
{
	/**Размер квадратной матрицы данных модели таблицы судоку.*/
	public static final int TABLE_SIZE = 9;
	/**Поле данных модели таблицы судоку.*/
    private String[][] data;
    public SudokuTableModel()
    {
    	data = new String[TABLE_SIZE][TABLE_SIZE];
    }
    /**Метод возвращает число столбцов. 
    @return число столбцов*/ 
    public int getColumnCount() 
    {
        return TABLE_SIZE;
    }
    /**Метод возвращает число строк. 
    @return число строк*/ 
    public int getRowCount() {
        return TABLE_SIZE;
    }
    /**Метод возвращает имя столбца по его номеру.
    @param col  номер столбца 
    @return имя столбца*/
    public String getColumnName(int col) 
    {
        return null;
    }
    /**Метод возвращает значение из модели по номеру столбца и строки. 
    @param row номер строки
    @param col номер столбца
    @return имя столбца*/
    public String getValueAt(int row, int col)
    {
    	if((row < 0)||(row >= TABLE_SIZE))
    	{
    		return null;
    	}
    	if((col < 0)||(col >= TABLE_SIZE))
    	{
    		return null;
    	}
        return data[row][col];
    }
    /**Переопределяем метод определяющий возможность редактирования ячейки. 
    @param rowIndex номер строки
    @param columnIndex номер столбца
    @return true*/
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
    	return true;
    }
    /**Переопределяем метод устанавливающий новое значение в модель. 
    @param rowIndex номер строки
    @param columnIndex номер столбца*/
    @Override
    public void setValueAt(Object aValue,
            int rowIndex,
            int columnIndex)
    {
    	if((rowIndex < 0)||(rowIndex >= TABLE_SIZE))
    	{
    		return;
    	}
    	if((columnIndex < 0)||(columnIndex >= TABLE_SIZE))
    	{
    		return;
    	}
    	data[rowIndex][columnIndex] = (String)aValue;
    }
    /**Метод очищает поля модели.*/
    public void clearModel()
    {
    	for(int i = 0; i < TABLE_SIZE; i++)
    	{
    		for(int j = 0; j < TABLE_SIZE; j++)
    		{
    			data[i][j] = null;
    		}
    	}
    }
    /**Метод загружает в модель данные из объекта класса SudokuField. 
    @param field объект класса SudokuField из которого мы загружаем данные*/
    public void fill(SudokuField field)
    {
    	for(int i = 0; i < TABLE_SIZE; i++)
	    {
	  	   for(int j = 0; j < TABLE_SIZE; j++)
	  	   {
	  		  
	  		  if(field.getCell(i, j).getValue() == 0)
	  		  {
	               data[i][j] = null;
	  		  }
	  		  else
	  		  {
	  			  Integer num = field.getCell(i, j).getValue();
	  			  data[i][j] = num.toString();
	  		  }
	  	   }
	    }
    }  
}

/**Класс представляющий таблицу.*/
class SudokuTable extends JTable 
{
	/**Ширина таблицы судоку.*/
	private int width;
	/**Высота таблицы судоку.*/
	private int height;
	public SudokuTable(SudokuTableModel model)
	{
         super(model);
	}
	/**Переопределяем метод отвечающий за отрисовку. 
    @param g объект содержащий инструменты для рисования*/
    @Override
    protected void paintComponent(Graphics g)
	{
    	  Graphics2D g2 = (Graphics2D)g;
	      super.paintComponent(g2);//Вызываем реализацию метода у суперкласса 
	      width = getWidth();
	      height = getHeight();
	      /*Меняем толщину линии и рисуем прямоугольник и прямые для выделения секторов поля судоку*/
	      if (g2 != null)
	      {
	    	    BasicStroke pen = new BasicStroke(3); 
	    	    g2.setStroke(pen);
	            g2.drawRect(0, 0, width - 1, height - 1);
	            
	            g2.drawLine(width / 3, 0, width / 3, height);
	            g2.drawLine(width / 3 * 2, 0, width / 3 * 2, height);
	            
	            g2.drawLine(0 , height / 3, width, height / 3);
	            g2.drawLine(0 , height / 3 * 2, width, height / 3 * 2);
	      }
	 }
}
/**Класс отвечающий за отображение данных в таблице.*/
class SudokuTableRenderer extends DefaultTableCellRenderer
{
	/**Значение свидетельствующее о том что был введен недопустимый символ*/
	public static final int INCORRECT_VALUE = -1;
	/**Толщина границы вокруг ячейки при введении в неё некорректного значения*/
	public static final int THICKNESS = 3;
	/**Переопределяем метод отвечающий за отрисовку ячеек таблицы таким образом
	  чтобы ячейки с некорректными значениями выделялись красным */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        //Вызываем реализацию метода у суперкласса
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        String str = (String)value;
        int n;
        try
	    {
	          n = Integer.parseInt(str);
	    }
        //Если преобразование невозможно
	    catch(NumberFormatException e)
	    {
		    n = INCORRECT_VALUE;
	    }
        if((n == INCORRECT_VALUE)&&(str != null)) 
        {
        	c.setBorder(new LineBorder(Color.RED, THICKNESS));
        }
        else
        {
        	if((n != INCORRECT_VALUE)&&((n < 1) || (n > 9)))
        	{
        		
        		c.setBorder(new LineBorder(Color.RED, THICKNESS));
        	}
        	else
        	{
        	   c.setBackground(new JLabel().getBackground());
        	}
        }
        return c;
    }
}

