package Presentation;

import Domain.Dish;
import Domain.DishOrder;
import Manager.DishManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import Manager.UIManager;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
/**
 *
 * @author Mathijs, Dennis
 */
public class SysteemUI extends JFrame
{
    private JFrame frame;
    private JPanel navBarPanel, orderedItemPanel, receiptPanel;
    private ArrayList<JPanel> panelList;
    private JTabbedPane menuTabbedPane;
    private UIManager manager;
    private DishManager dishManager;
    
    public SysteemUI()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        frame.setLayout(new BorderLayout());
        frame.setTitle("De Hartige Hap");
        
        panelList = new ArrayList<>();
        
        dishManager = new DishManager();
        
        //Navigation bar with buttons
        frame.add(new NavBarPanel(), BorderLayout.NORTH);
        
        //Menu with categories
        menuTabbedPane = new JTabbedPane();
        menuTabbedPane.add("Voorgerechten", new JScrollPane(new AppetizerPanel()));
        menuTabbedPane.add("Hoofdgerechten", new JScrollPane(new MainCoursePanel()));
        menuTabbedPane.add("Nagerechten", new JScrollPane(new DessertPanel()));
        menuTabbedPane.add("Dranken", new JScrollPane(new DrinkPanel()));
        frame.add(menuTabbedPane, BorderLayout.CENTER);
        
        //Menu with list of ordered items
        orderedItemPanel = new JPanel();
        panelList.add(orderedItemPanel);
        add(orderedItemPanel, BorderLayout.CENTER);
        orderedItemPanel.setVisible(false);
        
        //Menu with receipt
        receiptPanel = new JPanel();
        panelList.add(receiptPanel);
        add(receiptPanel, BorderLayout.CENTER);
        receiptPanel.setVisible(false);
        
        frame.pack();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    
    class NavBarPanel extends JPanel
    {   
        private JButton helpButton;
        
        public NavBarPanel()
        {
            setLayout(new BorderLayout());
            setBackground(Color.white);
            
            add(new NavButtonPanel(), BorderLayout.WEST);
            
            add(helpButton, BorderLayout.EAST);
        }
        
        class NavButtonPanel extends JPanel
        {
            private JButton menuButton, orderedItemButton, receiptButton;
            private ArrayList<JButton> navBarButtons;
            
            public NavButtonPanel()
            {
                setLayout(new GridLayout(1, 3));
                
                //Menu buttons
                menuButton = new JButton("Menu");
                menuButton.addActionListener((ActionEvent e) -> {
                    //Change active panel to the menu
                    for(JPanel panel : panelList)
                    {
                        panel.setVisible(false);
                    }
                    menuTabbedPane.setVisible(true);
                });
                
                orderedItemButton = new JButton("Bestelde Gerechten");
                orderedItemButton.addActionListener((ActionEvent e) -> {
                    changePanel(orderedItemPanel);
                });
                
                receiptButton = new JButton("Rekening");
                receiptButton.addActionListener((ActionEvent e) -> {
                   changePanel(receiptPanel);
                });
                
                helpButton = new JButton("?");
                
                //Array with all navbar buttons for styling and adding to the panel
                navBarButtons = new ArrayList<>();
                
                navBarButtons.add(menuButton);
                navBarButtons.add(orderedItemButton);
                navBarButtons.add(receiptButton);
                
                for(JButton button : navBarButtons)
                {
                    button.setMargin(new Insets(10, 10, 10, 10));
                    add(button);
                }
            }
        }
    }
    
    class AppetizerPanel extends JPanel
    {
        public AppetizerPanel()
        {
            //Display every appetizer
            for(Dish dish : dishManager.findDishes("Appetizer"))
            {
                add(createDishPanel(dish));
            }
        }
    }
    
    class MainCoursePanel extends JPanel
    {
        public MainCoursePanel()
        {
            //Display every main course
            for(Dish dish : dishManager.findDishes("MainCourse"))
            {
                add(createDishPanel(dish));
            }
        }
    }
    
    class DessertPanel extends JPanel
    {
        public DessertPanel()
        {
            //Display every dessert
            for(Dish dish : dishManager.findDishes("Dessert"))
            {
                add(createDishPanel(dish));
            }
        }
    }
    
    class DrinkPanel extends JPanel
    {
        public DrinkPanel()
        {
            //Display every drink
            for(Dish dish : dishManager.findDishes("Drink"))
            {
                add(createDishPanel(dish));
            }
        }
    }
    
    public void changePanel(JPanel panel)
    {
        //Set every panel to invisible except the given panel;
        for(JPanel panelInList : panelList)
        {
            if(panelInList != panel)
            {
                panelInList.setVisible(false);
            }
        }
        menuTabbedPane.setVisible(false);
        panel.setVisible(true);
    }
    
    public JPanel createDishPanel(Dish dish)
    {
        JPanel dishPanel = new JPanel();
        ArrayList<JLabel> labels = new ArrayList<>();
        
        dishPanel.setPreferredSize(new Dimension(300, 400));
        
        //Everything will be displayed vertically
        dishPanel.setLayout(new BorderLayout());
        
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(Color.white);
        
        labels.add(new JLabel(dish.getNameDish()));
        labels.add(new JLabel(dish.getDescriptionDish()));
        labels.add(new JLabel("€ " + String.format( "%.2f", dish.getpriceDish())));
        
        for(JLabel label : labels)
        {
            label.setAlignmentX(CENTER_ALIGNMENT);
            descriptionPanel.add(label);
        }
        
        JPanel orderPanel = new JPanel();
        orderPanel.setBackground(Color.white);
        JSpinner amountSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        orderPanel.add(amountSpinner);
        JButton addButton = new JButton("Voeg toe");
        addButton.addActionListener((ActionEvent e) -> {
            DishOrder newDish = new DishOrder(1, dish, (int)amountSpinner.getValue());
        });
        
        orderPanel.add(addButton);
        
        dishPanel.add(descriptionPanel, BorderLayout.CENTER);
        dishPanel.add(orderPanel, BorderLayout.SOUTH);
        
        return dishPanel;
    }
}
