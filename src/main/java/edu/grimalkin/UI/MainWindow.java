package edu.grimalkin.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.junrar.exception.RarException;

import edu.grimalkin.util.ZipUtil;
import edu.grimalkin.util.JSONUtil;
import edu.grimalkin.data.*;

/**
 * Une classe "MainWindow" représentant la fenêtre principale de l'application
 * La classe encapsule plusieurs méthodes permettant de manipuler les données de la fenêtre principale.
 * Une sélection d'objets Swing est mise en place pour permettre une manipulation plus aisée des composants de la fenêtre.
 */
public class MainWindow extends JFrame {
	/** Librairie de comics */
	private Library library; 
	/** Panel de la fenêtre principale */
	private JSplitPane splitPane = new JSplitPane();
	/** Panel de gauche */
	private JTabbedPane leftPane = new JTabbedPane();
	/** Panel de la librairie */
	private JPanel libraryPanel = new JPanel();
	/** ScrollPane de la librairie */
	private JScrollPane libraryScrollPane = new JScrollPane();
	/** Panel des vignettes */
	private JPanel thumbnailsPanel = new JPanel();
	/** Panel de droite */
	private JTabbedPaneCloseButton rightPane = new JTabbedPaneCloseButton();
	/** Panel de l'onglet de démarrage rapide */
	private JPanel quickstartPanel = new JPanel();
	/** Fenêtre de dialogue "Raccourcis clavier" */
	private JDialog shortcutsDialog = new JDialog();
	/** Fenêtre de dialogue "A propos" */
	private JDialog aboutDialog = new JDialog();
	/** Liste des raccourcis claviers des items du menu */
	private final JList<String> shortcutsList = new JList<>(new String[] {
		"Open: Ctrl + O",
		"Close: Ctrl + W",
		"Delete: Ctrl + D",
		"Exit: Ctrl + Q",
		"No Rotation (0): Ctrl + Alt + 1",
		"90 degrees clockwise (1): Ctrl + Alt + 2",
		"180 degrees (2): Ctrl + Alt + 3",
		"90 degrees counterclockwise (3): Ctrl + Alt + 4",
		"First page: Ctrl + B",
		"Previous page: Ctrl + P, Left arrow",
		"Next page: Ctrl + B, Right arrow",
		"Last page: Ctrl + L",
		"Go to page: Ctrl + G",
		"Zoom in: Ctrl + +",
		"Zoom out: Ctrl + -",
		"Custom zoom: Ctrl + 0",
		"Original fit: Ctrl + Alt + O",
		"Zoom to fit width: Ctrl + Alt + W",
		"Zoom to fit height: Ctrl + Alt + H",
		"Shortcuts: Ctrl + /"
	});

	/**
	 * Constructeur de la classe MainWindow
	 * @param title Titre de la fenêtre principale
	 */
    public MainWindow(String title) {
		try {
			library = JSONUtil.readJSONFile();
			// display comics infos in the library
			library.displayComics();
			library.initComics();
		} catch (IOException e) {
			e.printStackTrace();
		}
        initWindow(title);
        initMenuBar();
		initContent();
    }
 
	/**
	 * Méthode permettant d'initialiser la fenêtre principale
	 * @param title Titre de la fenêtre principale
	 */
    private void initWindow(String title) {
        setTitle(title);
		//set icon to /icons/chat3.png
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/chat3.png")));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
		setVisible(true);
		setFocusable(true);
		// add key event listener
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// call key event method
				keyIsPressed(e);
			}
		});
    }

	/**
	 * Méthode permettant d'initialiser la barre de menu
	 */
    private void initMenuBar() {
		// Components initialization
		JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
		JMenuItem openMenuItem = new JMenuItem();
		JMenuItem deleteMenuItem = new JMenuItem();
		JMenuItem closeMenuItem = new JMenuItem();
		JMenuItem exitMenuItem = new JMenuItem();
		JMenu editMenu = new JMenu();
		JMenu rotateMenu = new JMenu();
		JMenuItem noRotationMenuItem = new JMenuItem();
		JMenuItem rotate90MenuItem = new JMenuItem();
		JMenuItem rotate180MenuItem = new JMenuItem();
		JMenuItem rotate270MenuItem = new JMenuItem();
		JMenu readMenu = new JMenu();
		JMenuItem firstPageMenuItem = new JMenuItem();
		JMenuItem previousPageMenuItem = new JMenuItem();
		JMenuItem nextPageMenuItem = new JMenuItem();
		JMenuItem lastPageMenuItem = new JMenuItem();
		JMenuItem goToPageMenuItem = new JMenuItem();
		JMenu viewMenu = new JMenu();
		JMenuItem originalFitMenuItem = new JMenuItem();
		JMenuItem fitToWidthMenuItem = new JMenuItem();
		JMenuItem fitToHeightMenuItem = new JMenuItem();
		JMenu zoomMenu = new JMenu();
		JMenuItem zoomInMenuItem = new JMenuItem();
		JMenuItem zoomOutMenuItem = new JMenuItem();
		JMenuItem zoomCustomMenuItem = new JMenuItem();
		JMenu helpMenu = new JMenu();
		JMenuItem shortcutsMenuItem = new JMenuItem();
		JMenuItem aboutMenuItem = new JMenuItem();

        // Menu Bar Setup
		{
			// File Menu Setup
			{
				fileMenu.setText("File");
				fileMenu.setMnemonic((int)'F');
				// Open Menu Item Setup
				{
					openMenuItem.setText("Open...");
					openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					openMenuItem.setMnemonic((int)'O');
					openMenuItem.addActionListener(e -> openActionPerformed());
					fileMenu.add(openMenuItem);
				}
				// Delete Menu Item Setup
				{
					deleteMenuItem.setText("Delete");
					deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					deleteMenuItem.setMnemonic((int)'D');
					deleteMenuItem.addActionListener(e -> deleteActionPerformed());
					fileMenu.add(deleteMenuItem);
				}
				// Close Menu Item Setup
				{
					closeMenuItem.setText("Close");
					closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					closeMenuItem.setMnemonic((int)'C');
					closeMenuItem.addActionListener(e -> closeActionPerformed());
					fileMenu.add(closeMenuItem);
				}
				fileMenu.addSeparator();
				// Exit Menu Item Setup
				{
					exitMenuItem.setText("Exit");
					exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					exitMenuItem.setMnemonic('X');
					exitMenuItem.addActionListener(e -> exitActionPerformed());
					fileMenu.add(exitMenuItem);
				}
				menuBar.add(fileMenu);
			}
			// Edit Menu Setup
			{
				editMenu.setText("Edit");
				editMenu.setMnemonic((int)'E');
				// Rotate Menu Setup
				rotateMenu.setText("Rotate");
				rotateMenu.setMnemonic((int)'R');
				{
					// No Rotation Menu Item Setup 
					{
						noRotationMenuItem.setText("No Rotation");
						// Shortcut is CTRL ALT 1
						noRotationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
						noRotationMenuItem.addActionListener(e -> noRotationActionPerformed());
						rotateMenu.add(noRotationMenuItem);
					}		
					// Rotate 90 Menu Item Setup
					{
						rotate90MenuItem.setText("90\u00b0");
						// Shortcut is CTRL ALT 2
						rotate90MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
						rotate90MenuItem.addActionListener(e -> RotationActionPerformed(e));
						rotateMenu.add(rotate90MenuItem);
					}
					// Rotate 180 Menu Item Setup
					{
						rotate180MenuItem.setText("180\u00b0");
						// Shortcut is CTRL ALT 3
						rotate180MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
						rotate180MenuItem.addActionListener(e -> RotationActionPerformed(e));
						rotateMenu.add(rotate180MenuItem);
					}
					// Rotate 270 Menu Item Setup
					{
						rotate270MenuItem.setText("270\u00b0");
						// Shortcut is CTRL ALT 4
						rotate270MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
						rotate270MenuItem.addActionListener(e -> RotationActionPerformed(e));
						rotateMenu.add(rotate270MenuItem);
						editMenu.add(rotateMenu);
					}
				}
				menuBar.add(editMenu);
			}
			// Read Menu Setup
			{
				readMenu.setText("Read");
				readMenu.setMnemonic((int)'R');
				// First Page Menu Item Setup
				{
					firstPageMenuItem.setText("First Page");
					firstPageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					firstPageMenuItem.setMnemonic((int)'F');
					firstPageMenuItem.addActionListener(e -> goToFirstPageActionPerformed());
					readMenu.add(firstPageMenuItem);
				}
				// Previous Page Menu Item Setup
				{
					previousPageMenuItem.setText("Previous Page");
					previousPageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					previousPageMenuItem.setMnemonic((int)'P');
					previousPageMenuItem.addActionListener(e -> previousPageActionPerformed());
					readMenu.add(previousPageMenuItem);
				}
				// Next Page Menu Item Setup
				{
					nextPageMenuItem.setText("Next Page");
					nextPageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					nextPageMenuItem.setMnemonic((int)'N');
					nextPageMenuItem.addActionListener(e -> nextPageActionPerformed());
					readMenu.add(nextPageMenuItem);
				}
				// Last Page Menu Item Setup
				{
					lastPageMenuItem.setText("Last Page");
					lastPageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					lastPageMenuItem.setMnemonic((int)'L');
					lastPageMenuItem.addActionListener(e -> goToLastPageActionPerformed());
					readMenu.add(lastPageMenuItem);
				}
				// Go To Page Menu Item Setup
				{
					goToPageMenuItem.setText("Go To Page");
					goToPageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					goToPageMenuItem.setMnemonic((int)'G');
					goToPageMenuItem.addActionListener(e -> goToPageActionPerformed());
					readMenu.add(goToPageMenuItem);
				}
				menuBar.add(readMenu);
			}
			// View Menu Setup
			{
				viewMenu.setText("View");
				viewMenu.setMnemonic((int)'V');
				// Zoom Menu Setup
				zoomMenu.setText("Zoom");
				zoomMenu.setMnemonic((int)'Z');
				{
					// Zoom in
					zoomInMenuItem.setText("Zoom In");
					zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					zoomInMenuItem.setMnemonic((int)'I');
					zoomInMenuItem.addActionListener(e -> zoomInActionPerformed());
					zoomMenu.add(zoomInMenuItem);
					// Zoom out
					zoomOutMenuItem.setText("Zoom Out");
					zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					zoomOutMenuItem.setMnemonic((int)'O');
					zoomOutMenuItem.addActionListener(e -> zoomOutActionPerformed());
					zoomMenu.add(zoomOutMenuItem);
					// Zoom custom
					zoomCustomMenuItem.setText("Zoom Custom");
					zoomCustomMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
					zoomCustomMenuItem.setMnemonic((int)'C');
					zoomCustomMenuItem.addActionListener(e -> customZoomActionPerformed());
					zoomMenu.add(zoomCustomMenuItem);
					// separator
					zoomMenu.addSeparator();
					// Original fit (best view)
					originalFitMenuItem.setText("Original Fit");
					// Shortcut is CTRL + ALT + O
					originalFitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
					originalFitMenuItem.setMnemonic((int)'O');
					originalFitMenuItem.addActionListener(e -> originalFitActionPerformed());
					zoomMenu.add(originalFitMenuItem);
					// Zoom to fit width
					fitToWidthMenuItem.setText("Fit to Width");
					// Shortcut is CTRL + ALT + W
					fitToWidthMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
					fitToWidthMenuItem.setMnemonic((int)'W');
					fitToWidthMenuItem.addActionListener(e -> fitToWidthActionPerformed());
					zoomMenu.add(fitToWidthMenuItem);
					// Zoom to fit height
					fitToHeightMenuItem.setText("Fit to Height");
					// Shortcut is CTRL + ALT + H
					fitToHeightMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.ALT_DOWN_MASK));
					fitToHeightMenuItem.setMnemonic((int)'H');
					fitToHeightMenuItem.addActionListener(e ->fitToHeightActionPerformed());
					zoomMenu.add(fitToHeightMenuItem);
					viewMenu.add(zoomMenu);
				}
				menuBar.add(viewMenu);
			}
			// help menu
			{
				helpMenu.setText("Help");
				helpMenu.setMnemonic((int)'H');
				// Shortcuts
				shortcutsMenuItem.setText("Shortcuts");
				// Shortcut is CTRL + /
				shortcutsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

				shortcutsMenuItem.setMnemonic((int)'S');
				shortcutsMenuItem.addActionListener(e -> shortcutsActionPerformed());
				helpMenu.add(shortcutsMenuItem);
				// About
				aboutMenuItem.setText("About");
				aboutMenuItem.setMnemonic((int)'A');
				aboutMenuItem.addActionListener(e -> aboutActionPerformed());
				helpMenu.add(aboutMenuItem);
				menuBar.add(helpMenu);
			}
			setJMenuBar(menuBar); 
		}
    }

	/** 
	 * Mise en place des composants du panneau de gauche de l'interface graphique
	 */
	private void leftPaneSetup() {
		// add library panel and comic pages panel to left pane
		leftPane.addTab("Library", libraryPanel);
		// leftPane.addTab("Pages", comicPagesPanel);
		libraryPanel.setLayout(new BorderLayout());
		libraryPanel.add(libraryScrollPane, BorderLayout.CENTER);
		libraryScrollPane.setViewportView(thumbnailsPanel);
		// setup scroll bars
		libraryScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		libraryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// speed up scrolling
		libraryScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		// set layout for thumbnails panel;
		thumbnailsPanel.setLayout(new FlowLayout());
		// set thumbnailsPanel container size
		thumbnailsPanel.setPreferredSize(new Dimension(180, 0));
		// load library
		// for each comic in library
		for (Comic comic : library.getComics()) {
			displayThumbnail(comic, thumbnailsPanel);
		}
		splitPane.setLeftComponent(leftPane);
	}

	/** 
	 * Mise en place des composants du panneau de droite de l'interface graphique
	 */
	private void rightPaneSetup() {
		// add focus page panel to right pane
		rightPane.addTab("Quickstart", quickstartPanel);

		// set titled border
		quickstartPanel.setLayout(new BorderLayout());
		// List shortcuts on Quickstart tab
		shortcutsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shortcutsList.setLayoutOrientation(JList.VERTICAL);
		shortcutsList.setVisibleRowCount(-1);
		quickstartPanel.add(new JScrollPane(shortcutsList), BorderLayout.CENTER);
		splitPane.setRightComponent(rightPane);	
	}
	
	/**
	 * Méthode permettant d'initialiser le contenu de la fenêtre
	 * Appelle les méthodes de mise en place des composants de l'interface graphique
	 * @see #leftPaneSetup()
	 * @see #rightPaneSetup()
	 */
	private void initContent() {
		splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		splitPane.setResizeWeight(0.2);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		// Left Panel Setup
		leftPaneSetup();
		
		// Right Panel Setup
		rightPaneSetup();
	}

	/**
	 * Méthode permettant de mettre à jour la bibliothèque de comics en sauvegardant les modifications dans le fichier JSON
	 * @see JSONUtil#writeJSONFile(Library)
	 */
	private void updateLibrary() {
		try {
			JSONUtil.writeJSONFile(library);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant de mettre à jour un comic dans la bibliothèque
	 * @param title Titre du comic à mettre à jour
	 * @param currentPage Page courante du comic
	 * @see #updateLibrary()
	 */
	private void updateComic(String title, int currentPage) {
		Comic comic = library.getComic(title);
		comic.setCurrentPage(currentPage);
		comic.setLastPageRead(currentPage);
		System.out.println("Updating comic " + title + " to page " + currentPage);
		updateLibrary();
	}

	/**
	 * Méthode permettant d'afficher un comic dans le panneau de droite
	 * @param comic Comic à afficher
	 * @see #updateComic(String, int)
	 */
	private void displayThumbnail(Comic comic, JPanel thumbnailsPanel) {
		// Create new thumbnail
		JLabel thumbnail = new JLabel();
		thumbnail.setName(comic.getTitle());
		// set titled border
		thumbnail.setBorder(BorderFactory.createTitledBorder(comic.getTitle()));
		ImageIcon icon = new ImageIcon(comic.getCover());
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(100, -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		thumbnail.setIcon(icon);
		// add event listener to thumbnail
		thumbnail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if left mouse button is clicked
				if (e.getButton() == MouseEvent.BUTTON1) {
					// if comic is already open
					for (int i = 0; i < rightPane.getTabCount(); i++) {
						if (rightPane.getTitleAt(i).equals(comic.getTitle())) {
							// select tab
							rightPane.setSelectedIndex(i);
							return;
						}
					}
					// if comic is not open
					// add new tab to right pane
					JPanel comicPagesPanel = new JPanel();

					JScrollPane comicPageScrollPane = new JScrollPane();

					JPanel comicPagePanel = new JPanel();
					{
						comicPagesPanel.setLayout(new BorderLayout());
						rightPane.addTab(comic.getTitle(), comicPagesPanel);
						rightPane.setSelectedIndex(rightPane.getTabCount() - 1);
					}
					// Comics Pane Setup
					{
						comicPagesPanel.setLayout(new BorderLayout());
						comicPageScrollPane.setBorder(BorderFactory.createTitledBorder("Page 1"));
						comicPageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						comicPageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						// Speed up scrolling
						comicPageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
						comicPageScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
						comicPageScrollPane.setViewportView(comicPagePanel);
						comicPagePanel.setLayout(new BorderLayout());
					}
					comicPagesPanel.add(comicPageScrollPane, BorderLayout.CENTER);
					// add comic pages to comic pages panel
					Comic comic = library.getComic(thumbnail.getName());
					// Get Comic Page
					Page page = comic.getPages().get(comic.getCurrentPage());
					// Display Comic Page
					displayPage(page, comicPagePanel);
				}
			}
		});
		// add thumbnail to thumbnail panel
		thumbnailsPanel.add(thumbnail);
		// Update thumbnails panel
		thumbnailsPanel.revalidate();
		thumbnailsPanel.repaint();
	}

	/**
	 * Méthode permettant d'afficher une page dans le panneau de droite
	 * @param page Page à afficher
	 * @param comicPagePanel Panneau de droite
	 * @see #displayThumbnail(Comic, JPanel)
	 * @see #displayPage(Page, JPanel)
	 */
	private void displayPage(Page page, JPanel comicPagePanel) {
		// Get Scroll Pane from comicPagePanel
		JScrollPane comicPageScrollPane = (JScrollPane) ((JPanel) rightPane.getComponentAt(rightPane.getSelectedIndex())).getComponent(0);
		// Get Scroll Pane width
		// Remove all components from comicPagePanel
		comicPagePanel.removeAll();
		// wait for components to init
		int width = comicPageScrollPane.getWidth();
		// Get Comic Page Image
		ImageIcon icon = new ImageIcon(page.getImage());
		// Resize only image width and keep aspect ratio	
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(width-30, -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// New Comic Page Label
		JLabel comicPageLabel = new JLabel();
		// Add event listener to comic page label, on mouse click
		comicPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if left mouse button is clicked
				if (e.getButton() == MouseEvent.BUTTON1) {
					// Open Comic Page in new window
					JFrame comicPageFrame = new JFrame();
					comicPageFrame.setTitle("Comic Page");
					comicPageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					comicPageFrame.setSize(600, 800);
					comicPageFrame.setLocationRelativeTo(null);
					comicPageFrame.setVisible(true);	
					// Add Scroll Pane to Comic Page Frame
					JScrollPane comicPageScrollPane = new JScrollPane();
					comicPageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					comicPageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					// Speed up scrolling
					comicPageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
					comicPageScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
					comicPageFrame.add(comicPageScrollPane);
					// Add Comic Page Panel to Scroll Pane
					JPanel comicPagePanelFullscreen = new JPanel();
					comicPagePanelFullscreen.setLayout(new BorderLayout());
					comicPageScrollPane.setViewportView(comicPagePanelFullscreen);
					// Add Comic Page Label to Comic Page Panel
					JLabel comicPageLabelFullscreen = new JLabel();
					comicPageLabelFullscreen.setIcon(comicPageLabel.getIcon());
					comicPagePanelFullscreen.add(comicPageLabelFullscreen, BorderLayout.CENTER);
					// Update Comic Page Frame
					comicPageFrame.revalidate();
					comicPageFrame.repaint();
					// On resize, resize comic page label
					comicPageFrame.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentResized(ComponentEvent e) {
							// Get Comic Page Image
							// Get tab pane
							JTabbedPane tabbedPane = (JTabbedPane) comicPagePanel.getParent().getParent().getParent().getParent();
							// Get tab name
							String tabName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
							// Get Comic
							Comic comic = library.getComic(tabName);
							// Get Comic Page
							Page page = comic.getPages().get(comic.getCurrentPage());
							ImageIcon icon = new ImageIcon(page.getImage());
							// Resize only image width and keep aspect ratio	
							Image img = icon.getImage();
							Image newimg = img.getScaledInstance(comicPageFrame.getWidth()-30, -1,  java.awt.Image.SCALE_SMOOTH);
							icon = new ImageIcon(newimg);
							// Set Comic Page Label Icon
							comicPageLabelFullscreen.setIcon(icon);
							// Update Comic Page Label
							comicPageLabelFullscreen.revalidate();
							comicPageLabelFullscreen.repaint();
						}
					});
				}
			}
		});
		// Add event listener to comic page label, on mouse wheel scroll while CTRL is pressed
		// Ajouté après la revue du 15/12/2022 à 10h 
		comicPageLabel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// if CTRL is pressed
				if (e.isControlDown()) {
					// Get Comic Page Image
					// Get tab pane
					JTabbedPane tabbedPane = (JTabbedPane) comicPagePanel.getParent().getParent().getParent().getParent();
					// Get tab name
					String tabName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
					// Get Comic
					Comic comic = library.getComic(tabName);
					// Get Comic Page
					Page page = comic.getPages().get(comic.getCurrentPage());
					ImageIcon icon = new ImageIcon(page.getImage());
					// Resize only image width and keep aspect ratio	
					Image img = icon.getImage();
					Image newimg = img.getScaledInstance(comicPageLabel.getWidth()+(e.getWheelRotation()*500), -1,  java.awt.Image.SCALE_SMOOTH);
					icon = new ImageIcon(newimg);
					// Set Comic Page Label Icon
					comicPageLabel.setIcon(icon);
					// Update Comic Page Label
					comicPageLabel.revalidate();
					comicPageLabel.repaint();
				}
			}
		});
		// Set Comic Page Label Icon
		comicPageLabel.setIcon(icon);
		// Add Comic Page Label to Comic Page Panel
		comicPagePanel.add(comicPageLabel, BorderLayout.CENTER);
		// add event listener to scroll pane to change page when scroll further than the end of the page

		// Update Comic Page Panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}
	

	/**
	 * Méthode permettant d'afficher une page dans le panneau de droite
	 * @param page Page à afficher
	 * @param comicPagePanel Panneau de droite
	 * @see ZipUtil#unzip(File, List<Page>)
	 * @see ZipUtil#unrar(File, List<Page>)
	 * @see #displayThumbnail(Comic, JPanel)
	 * @see #displayPage(Page, JPanel)
	 */
	private void openActionPerformed() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Comic Book Archive (.cbz, .cbr)", "cbz", "cbr");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			for (Comic comic : library.getComics()) {
				System.out.println("Checking if comic is already in library.");
				System.out.println("Comic: " + comic.getTitle());
				System.out.println("File: " + file.getName());
				if (comic.getTitle().equals(file.getName())) {
					// Display dialog box
					JOptionPane.showMessageDialog(this, "Comic already in library. Please open it from there.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			JPanel newTab = new JPanel();
			JScrollPane comicPageScrollPane = new JScrollPane();
			JPanel comicPagePanel = new JPanel();
			System.out.println("Opening: " + file.getName() + ".");
			// New Tab Setup
			{
				newTab.setLayout(new BorderLayout());
				rightPane.addTab(file.getName(), newTab);
				rightPane.setSelectedIndex(rightPane.getTabCount() - 1);
			}
			// Comics Pane Setup
			{
				newTab.setLayout(new BorderLayout());
				comicPageScrollPane.setBorder(BorderFactory.createTitledBorder("Page 1"));
				comicPageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				comicPageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				// Speed up scrolling
				comicPageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
				comicPageScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
				comicPageScrollPane.setViewportView(comicPagePanel);
				comicPagePanel.setLayout(new BorderLayout());
			}
			newTab.add(comicPageScrollPane, BorderLayout.CENTER);
			try {
				List<Page> pages = new ArrayList<Page>();
				if (file.getName().endsWith(".cbz")) {
					ZipUtil.unzip(file, pages);
				} else if (file.getName().endsWith(".cbr")) {
					ZipUtil.unrar(file, pages);
				}
				Comic unzippedComic = new Comic(file.getName(), file.getAbsolutePath(), pages);
				library.addComic(unzippedComic);
				updateLibrary();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RarException e) {
				e.printStackTrace();
			}
			// Display New Comic in Library
			{
				displayThumbnail(library.getComic(file.getName()), thumbnailsPanel);
			}
			// Display Comic in New Tab
			{
				// Get Comic
				System.out.println(file.getName());
				// Print Pages Ids to console
				for (int i = 0; i < library.getComic(file.getName()).getPages().size(); i++) {
					System.out.println(library.getComic(file.getName()).getPages().get(i).getId());
				}
				Comic comic = library.getComic(file.getName());
				// Get Comic Page
				Page page = comic.getPages().get(comic.getCurrentPage());
				// Display Comic Page
				displayPage(page, comicPagePanel);
			}
		}
	}

	/**
	 * Méthode permettant de supprimer le comic courant de la librairie
	 * @see #updateLibrary()
	 */
	private void deleteActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// remove comic from library
		library.removeComic(selectedTabTitle);
		// remove comic from right pane
		rightPane.remove(rightPane.getSelectedIndex());
		// remove comic thumbnail from left pane
		// get component index 
		int componentIndex = 0;
		for (int i = 0; i < thumbnailsPanel.getComponentCount(); i++) {
			if (thumbnailsPanel.getComponent(i).getName().equals(selectedTabTitle)) {
				componentIndex = i;
			}
		}
		thumbnailsPanel.remove(thumbnailsPanel.getComponent(componentIndex));
		// update library
		updateLibrary();
	}


    /**
	 * Méthode permettant de ferme une page dans le panneau de droite
	 * Sauvegarde la page courante du comic
	 * @see #updateComic(String, int)
	 */
	private void closeActionPerformed() {
		// get selected tab 
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// save comic current page
		updateComic(selectedTabTitle, library.getComic(selectedTabTitle).getCurrentPage());
		// Close Tab
		rightPane.remove(rightPane.getSelectedIndex());
	}

	/**
	 * Méthode permettant de fermer toutes les pages dans le panneau de droite ainsi que la fenêtre
	 * Sauvegarde la page courante de chaque comic
	 * @see #closeAllTabs()
	 */
	private void exitActionPerformed() {
		// Close all tabs
		closeAllTabs();
		// Exit
		System.exit(0);
	}

	/**
	 * Méthode permettant d'annuler la rotation d'une page
	 */
	private void noRotationActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// set image icon to original image icon
		((JLabel) ((JPanel) ((JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0)).getViewport().getView()).getComponent(0)).setIcon(((JLabel) ((JPanel) ((JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0)).getViewport().getView()).getComponent(0)).getIcon());
	}

	/**
	 * Méthode permettant de faire une rotation d'une page de e degrés
	 * @param e ActionEvent
	 */
	private void RotationActionPerformed(ActionEvent e) {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get panel from selected tab
		JPanel panel = (JPanel) rightPane.getComponentAt(selectedTab);
		// get scroll pane from panel
		JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
		// get panel from scroll pane
		JPanel pagePanel = (JPanel) scrollPane.getViewport().getView();
		// get image label from panel
		JLabel imageLabel = (JLabel) pagePanel.getComponent(0);
		// get image icon from image label
		ImageIcon icon = (ImageIcon) imageLabel.getIcon();
		// rotate image icon by e degrees
		Image img = icon.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null); 
		System.out.println(width + " " + height);
		// put newimg in a buffered image
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		AffineTransform tx = new AffineTransform();
		if (e.getActionCommand().equals("90°")) {
			tx.rotate(Math.toRadians(90), width/2, height/2);
		} else if (e.getActionCommand().equals("180°")) {
			tx.rotate(Math.toRadians(180), width/2, height/2);
		} else if (e.getActionCommand().equals("270°")) {
			tx.rotate(Math.toRadians(270), width/2, height/2);
		}
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		op.filter(bimage, out);
		img = out;
		icon = new ImageIcon(img);
		// update image icon
		imageLabel.setIcon(icon);
	}

	/**
	 * Méthode permettant de passer à la page suivante
	 * Sauvegarde la page courante du comic
	 * @see #updateComic(String, int)
	 * @see Comic#nextPage()
	 */
	private void nextPageActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// save comic current page
		updateComic(selectedTabTitle, library.getComic(selectedTabTitle).getCurrentPage());
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		if (comic.getCurrentPage()+1 >= comic.getPageCount()) {
			// Display error message popup
			JOptionPane.showMessageDialog(null, "This is the last page", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			// next page 
			comic.nextPage();
			// get comic page
			Page page = comic.getPages().get(comic.getCurrentPage());
			// get comic page image
			ImageIcon icon = new ImageIcon(page.getImage());
			// resize only image width and keep aspect ratio
			Image img = icon.getImage();
			// get comic page scroll panel 
			JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
			// get comic page scroll panel width
			int width = comicPageScrollPanel.getWidth();
			Image newimg = img.getScaledInstance(width-30, -1,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(newimg);
			// get comic page panel
			JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
			// get comic page label from comic page panel
			JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
			// set comic page label icon
			comicPageLabel.setIcon(icon);
			// update comic page panel
			comicPagePanel.revalidate();
			comicPagePanel.repaint();
			updateComic(selectedTabTitle, comic.getCurrentPage());
		}
	}

	/**
	 * Méthode permettant de passer à la page précédente
	 * Sauvegarde la page courante du comic
	 * @see #updateComic(String, int)
	 * @see Comic#previousPage()
	 */
	private void previousPageActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// save comic current page
		updateComic(selectedTabTitle, library.getComic(selectedTabTitle).getCurrentPage());
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		if (comic.getCurrentPage()-1 < 0) {
			// Display error message popup
			JOptionPane.showMessageDialog(null, "This is the first page", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			// previous page 
			comic.previousPage();
			// get comic page
			Page page = comic.getPages().get(comic.getCurrentPage());
			// get comic page image
			ImageIcon icon = new ImageIcon(page.getImage());
			// resize only image width and keep aspect ratio
			Image img = icon.getImage();
			// get comic page scroll panel 
			JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
			// get comic page scroll panel width
			int width = comicPageScrollPanel.getWidth();
			Image newimg = img.getScaledInstance(width-30, -1,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(newimg);
			// get comic page panel
			JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
			// get comic page label from comic page panel
			JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
			// set comic page label icon
			comicPageLabel.setIcon(icon);
			// update comic page panel
			comicPagePanel.revalidate();
			comicPagePanel.repaint();
			updateComic(selectedTabTitle, comic.getCurrentPage());
		}
	}

	/**
	 * Méthode permettant de passer à la première page
	 * Sauvegarde la page courante du comic
	 * @see #updateComic(String, int)
	 * @see Comic#setCurrentPage(int)
	 */
	private void goToFirstPageActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// save comic current page
		updateComic(selectedTabTitle, library.getComic(selectedTabTitle).getCurrentPage());
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// go to first page 
		comic.setCurrentPage(0);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page image
		ImageIcon icon = new ImageIcon(page.getImage());
		// resize only image width and keep aspect ratio
		Image img = icon.getImage();
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page scroll panel width
		int width = comicPageScrollPanel.getWidth();
		Image newimg = img.getScaledInstance(width-30, -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
		updateComic(selectedTabTitle, comic.getCurrentPage());
	}

	/**
	 * Méthode permettant de passer à la dernière page
	 * Sauvegarde la page courante du comic
	 * @see #updateComic(String, int)
	 * @see Comic#setCurrentPage(int)
	 */
	private void goToLastPageActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// save comic current page
		updateComic(selectedTabTitle, library.getComic(selectedTabTitle).getCurrentPage());
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// go to last page 
		comic.setCurrentPage(comic.getPages().size() - 1);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page image
		ImageIcon icon = new ImageIcon(page.getImage());
		// resize only image width and keep aspect ratio
		Image img = icon.getImage();
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		Image newimg = img.getScaledInstance(600, -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
		updateComic(selectedTabTitle, comic.getCurrentPage());
	}

	/**
	 * Méthode permettant d'aller à une page donnée
	 */
	private void goToPageActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// Ask user for page number
		String input = JOptionPane.showInputDialog("Page number :");
		// if user input is not null
		if (input != null) {
			// if user input is a number
			if (input.matches("[0-9]+")) {
				// if user input is a valid page number
				if (Integer.parseInt(input) <= comic.getPages().size() && Integer.parseInt(input) > 0) {
					// save comic current page
					updateComic(selectedTabTitle, library.getComic(selectedTabTitle).getCurrentPage());
					// go to page
					comic.setCurrentPage(Integer.parseInt(input) - 1);
					// get comic page
					Page page = comic.getPages().get(comic.getCurrentPage());
					// get comic page image
					ImageIcon icon = new ImageIcon(page.getImage());
					// resize only image width and keep aspect ratio
					Image img = icon.getImage();
					// get comic page scroll panel 
					JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
					int width = comicPageScrollPanel.getWidth();
					Image newimg = img.getScaledInstance(width-30, -1,  java.awt.Image.SCALE_SMOOTH);
					icon = new ImageIcon(newimg);
					// get comic page panel
					JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
					// get comic page label from comic page panel
					JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
					// set comic page label icon
					comicPageLabel.setIcon(icon);
					// update comic page panel
					comicPagePanel.revalidate();
					comicPagePanel.repaint();
					updateComic(selectedTabTitle, comic.getCurrentPage());
				} else {
					JOptionPane.showMessageDialog(null, "Invalid page number", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Invalid page number", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

	/**
	 * Méthode permettant le zoom avant sur la page courante de l'onglet sélectionné
	 */
	private void zoomInActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page icon from panel
		ImageIcon icon = (ImageIcon) comicPageLabel.getIcon();
		// zoom in
		Image newimg = page.getImage().getScaledInstance((int) (icon.getIconWidth() * 1.1), -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}

	/**
	 * Méthode permettant le zoom arrière sur la page courante de l'onglet sélectionné
	 */
	private void zoomOutActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page icon from panel
		ImageIcon icon = (ImageIcon) comicPageLabel.getIcon();
		// zoom in
		Image newimg = page.getImage().getScaledInstance((int) (icon.getIconWidth() * 0.9), -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}

	/**
	 * Méthode permettant de zoomer sur la page courante de l'onglet sélectionné
	 */
	private void customZoomActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page icon from panel
		ImageIcon icon = (ImageIcon) comicPageLabel.getIcon();
		// get custom zoom value
		String customZoomValue = JOptionPane.showInputDialog("Enter custom zoom value (0.1 - 10.0):");
		// zoom custom
		Image newimg = page.getImage().getScaledInstance((int) (icon.getIconWidth() * Double.parseDouble(customZoomValue)), -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}

	/**
	 * Méthode permettant de mettre la page courante de l'onglet sélectionné à la taille la largeur de l'onglet
	 */
	private void fitToWidthActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page icon from panel
		ImageIcon icon = (ImageIcon) comicPageLabel.getIcon();
		// get scroll pane width
		int scrollPaneWidth = comicPageScrollPanel.getWidth();
		// get scroll pane height
		int scrollPaneHeight = comicPageScrollPanel.getHeight();
		// get image width
		int imageWidth = page.getImage().getWidth(null);
		// get image height
		int imageHeight = page.getImage().getHeight(null);
		// get image aspect ratio
		double imageAspectRatio = (double) imageWidth / (double) imageHeight;
		// get scroll pane aspect ratio
		double scrollPaneAspectRatio = (double) scrollPaneWidth / (double) scrollPaneHeight;
		// zoom to width only
		System.out.println("image width: " + imageWidth);
		System.out.println("image height: " + imageHeight);
		System.out.println("image aspect ratio: " + imageAspectRatio);
		System.out.println("scroll pane width: " + scrollPaneWidth);
		System.out.println("scroll pane height: " + scrollPaneHeight);
		System.out.println("scroll pane aspect ratio: " + scrollPaneAspectRatio);
		Image newimg = page.getImage().getScaledInstance(scrollPaneWidth-20, -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}

	/**
	 * Méthode permettant de mettre la page courante de l'onglet sélectionné à la taille la hauteur de l'onglet
	 */
	private void fitToHeightActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page icon from panel
		ImageIcon icon = (ImageIcon) comicPageLabel.getIcon();
		// get scroll pane width
		int scrollPaneWidth = comicPageScrollPanel.getWidth();
		// get scroll pane height
		int scrollPaneHeight = comicPageScrollPanel.getHeight();
		// get image width
		int imageWidth = page.getImage().getWidth(null);
		// get image height
		int imageHeight = page.getImage().getHeight(null);
		// get image aspect ratio
		double imageAspectRatio = (double) imageWidth / (double) imageHeight;
		// get scroll pane aspect ratio
		double scrollPaneAspectRatio = (double) scrollPaneWidth / (double) scrollPaneHeight;
		System.out.println("image width: " + imageWidth);
		System.out.println("image height: " + imageHeight);
		System.out.println("image aspect ratio: " + imageAspectRatio);
		System.out.println("scroll pane width: " + scrollPaneWidth);
		System.out.println("scroll pane height: " + scrollPaneHeight);
		System.out.println("scroll pane aspect ratio: " + scrollPaneAspectRatio);
		// zoom to height
		Image newimg = page.getImage().getScaledInstance(-1, scrollPaneHeight-50,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}

	/**
	 * Méthode permettant de mettre la page courante de l'onglet sélectionné à sa taille originale
	 */
	private void originalFitActionPerformed() {
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		// get comic page scroll panel 
		JScrollPane comicPageScrollPanel = (JScrollPane) ((JPanel) rightPane.getComponentAt(selectedTab)).getComponent(0);
		// get comic page panel
		JPanel comicPagePanel = (JPanel) comicPageScrollPanel.getViewport().getView();
		// get comic page label from comic page panel
		JLabel comicPageLabel = (JLabel) comicPagePanel.getComponent(0);
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get comic page
		Page page = comic.getPages().get(comic.getCurrentPage());
		// get comic page icon from panel
		ImageIcon icon = (ImageIcon) comicPageLabel.getIcon();
		// zoom to original fit
		Image newimg = page.getImage().getScaledInstance(-1, -1,  java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		// set comic page label icon
		comicPageLabel.setIcon(icon);
		// update comic page panel
		comicPagePanel.revalidate();
		comicPagePanel.repaint();
	}

	/**
	 * Méthode permettant de mettre la page courante de l'onglet sélectionné à la taille de l'écran
	 */
	private void shortcutsActionPerformed() {
		// set shortcuts dialog title
		shortcutsDialog.setTitle("Shortcuts");
		// set shortcuts dialog size
		shortcutsDialog.setSize(400, 400);
		// set shortcuts dialog layout
		shortcutsDialog.setLayout(new BorderLayout());
		// create shortcuts text area
		JTextArea shortcutsTextArea = new JTextArea();
		// set shortcuts text area non editable
		shortcutsTextArea.setEditable(false);
		// JList to list
		// List<String> list = new ArrayList<String>();
		// add each line of JList to list
		for (int i = 0; i < shortcutsList.getModel().getSize(); i++) {
			shortcutsTextArea.append(shortcutsList.getModel().getElementAt(i) + "\n");
		}
		// add shortcuts text area to shortcuts dialog
		shortcutsDialog.add(shortcutsTextArea, BorderLayout.CENTER);
		// set shortcuts dialog visible
		// set shrortcuts dialog location
		shortcutsDialog.setLocationRelativeTo(null);
		shortcutsDialog.setVisible(true);
	}

	/**
	 * Méthode permettant de mettre la page courante de l'onglet sélectionné à la taille de l'écran
	 */
	private void aboutActionPerformed() {
		// set about dialog title
		aboutDialog.setTitle("About");
		// set about dialog size
		aboutDialog.setSize(400, 400);
		// set about dialog layout
		aboutDialog.setLayout(new BorderLayout());
		// create about text area
		JTextArea aboutTextArea = new JTextArea();
		// set about text area non editable
		aboutTextArea.setEditable(false);
		// add about text to about text area
		aboutTextArea.setText(
		"Grimalkin v1.0\n"
		+ "Created by Dimitri HUDE and Laure BLANCHARD\n"
		+ "2022\n"
		+ "Contact:\n"
		+ "dimitri.hude@etu.univ-amu.fr\n"
		+ "laure.blanchard.1@etu.univ-amu.fr\n"
		+ "Please do not steal or reproduce this software without our permission.\n"
		);					
		// add about text area to about dialog
		aboutDialog.add(aboutTextArea, BorderLayout.CENTER);
		// set about dialog location
		aboutDialog.setLocationRelativeTo(null);
		// set about dialog visible
		aboutDialog.setVisible(true);
	}

	/**
	 * Méthode permettant de fermer tous les onglets en sauvegardant leur page courante, sauf pour l'onglet "Quickstart".
	 * Cette méthode est appelée lors de la fermeture de l'application.
	 */
	private void closeAllTabs() {
		// go through all tabs
		for (int i = 0; i < rightPane.getTabCount(); i++) {
			// if tab title is "Quickstart"
			if (rightPane.getTitleAt(i).equals("Quickstart")) {
				// remove tab
				rightPane.remove(i);
			} else {
				// get tab title
				String tabTitle = rightPane.getTitleAt(i);
				updateComic(tabTitle, library.getComic(tabTitle).getCurrentPage());
				rightPane.remove(i);
			}
		}
	}

	/**
	 * Méthode permettant de mettre à jour la bibliothèque en sauvegardant les informations de chaque comic.
	 * Cette méthode est appelée lors de la fermeture de l'application.
	 * @param e Evenement de fermeture de la fenêtre
	 */
	public void windowClosing(WindowEvent e) {
		// save library
		updateLibrary();
		// close all tabs
		closeAllTabs();
		// dispose dialogs
		// dispose shortcuts dialog
		shortcutsDialog.dispose();
		// dispose about dialog
		aboutDialog.dispose();
		// dispose frame
		dispose();
	}
	
	/** 
	 * Méthode de changer de page en appuyant sur les flèches du clavier.
	 * Note : cette méthode a été patchée après la revue du 15/12/2022 le matin.
	 * @param e Evenement de pression d'une touche du clavier
	 */
	public void keyIsPressed(KeyEvent e) {
		
		// get selected tab
		int selectedTab = rightPane.getSelectedIndex();
		// get selected tab title
		String selectedTabTitle = rightPane.getTitleAt(selectedTab);
		if (selectedTabTitle.equals("Quickstart")) {
			return;
		}
		// get comic
		Comic comic = library.getComic(selectedTabTitle);
		// get key code
		int keyCode = e.getKeyCode();
		// if key code is right arrow
		if (keyCode == KeyEvent.VK_RIGHT) {
			// if current page is not the last page
			if (comic.getCurrentPage() < comic.getPages().size() - 1) {
				nextPageActionPerformed();
			}
		// if key code is left arrow
		} else if (keyCode == KeyEvent.VK_LEFT) {
			// if current page is not the first page
			if (comic.getCurrentPage() > 0) {
				previousPageActionPerformed();
			}
		}
	}
}

		

