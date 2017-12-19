package com.app.main;

import java.io.File;

public class MainContainerPanel extends JPanel implements  StatusProgressListener{

	private JProgressBar jp;

	public MainContainerPanel() {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		setLayout(null);

		createComponents();
		setBoundsAndAddCompontnts();
		addEventListenersToComponents();
		
		updateTaskTree();

	}

	private JPanel titlePanel;
	private JPanel rightContainerPanel;
	private JLabel browseFilePathLabel;
	private JButton browseButton;
	private JLabel progressLabel;
	private JButton executeButton;
	private JPanel leftContainerPanel;
	private JScrollPane scrollPane;
	private JTree tree;
	private DefaultMutableTreeNode rootNode ;
	private List<TaskVO> taskList = null;
	
	private void createComponents() {
		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		
		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(MainContainerPanel.class.getResource("/com/app/main/logo5.png")));
		lblLogo.setBounds(0, 0, 224, 66);
		titlePanel.add(lblLogo);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(336, 26, 224, 14);
		titlePanel.add(lblNewLabel);

		rightContainerPanel = new JPanel();
		rightContainerPanel.setLayout(null);

		jp = new JProgressBar(0, 100);
		jp.setOpaque(false);
		jp.setFocusable(false);
		jp.setBorderPainted(false);
		jp.setUI(new ProgressCircleUI());

		browseFilePathLabel = new JLabel("");
//		browseFilePathLabel.setBackground(Color.LIGHT_GRAY);
//		browseFilePathLabel.setOpaque(true);

		browseButton = new JButton("Browse");

		progressLabel = new JLabel("");
//		progressLabel.setBackground(Color.LIGHT_GRAY);
//		progressLabel.setOpaque(true);

		executeButton = new JButton("Execute");

		leftContainerPanel = new JPanel();
		leftContainerPanel.setLayout(null);

		scrollPane = new JScrollPane();

		rootNode = new DefaultMutableTreeNode("Configured Tasks"); 
		tree = new JTree(rootNode);
		scrollPane.setViewportView(tree);

	}

	private void setBoundsAndAddCompontnts() {
		titlePanel.setBounds(0, 0, 600, 66);
		add(titlePanel);

		rightContainerPanel.setBounds(230, 69, 370, 281);
		add(rightContainerPanel);

		jp.setBounds(10, 123, 150, 150);
		rightContainerPanel.add(jp);

		browseFilePathLabel.setBounds(10, 26, 248, 14);
		rightContainerPanel.add(browseFilePathLabel);

		browseButton.setBounds(269, 22, 91, 23);
		rightContainerPanel.add(browseButton);

		progressLabel.setBounds(10, 57, 350, 14);
		rightContainerPanel.add(progressLabel);

		executeButton.setBounds(269, 82, 91, 23);
		rightContainerPanel.add(executeButton);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//reset action--------------------
			}
		});
		btnReset.setBounds(269, 123, 91, 23);
		rightContainerPanel.add(btnReset);

		leftContainerPanel.setBounds(0, 69, 226, 281);
		add(leftContainerPanel);

		scrollPane.setBounds(10, 11, 206, 259);
		leftContainerPanel.add(scrollPane);

	}

	private void addEventListenersToComponents() {
		browseButton.addActionListener(new FileChooserActionListener(this));
		executeButton.addActionListener(new ExecuteTaskActionListener(this));
	}

	private void updateTaskTree() {
		new Thread(new Runnable(){

			@Override
			public void run() {
				List<TaskVO> listTasksVO = getTaskVO();
				
				DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Update");
				rootNode.add(parent);

				DefaultMutableTreeNode tn = null;

				if(listTasksVO == null || listTasksVO.size()==0){
					tn = new DefaultMutableTreeNode("No task configured");
					parent.add(tn);
					
				}else{
					for (TaskVO vo : listTasksVO) {
						tn = new DefaultMutableTreeNode(vo.getTaskName());
						parent.add(tn);
						
					}
				}
			
				tree.revalidate();
				tree.updateUI();
				
			}}).start();
		

	}

	public List<TaskVO> getTaskVO() {
		
		if(taskList != null){
			return taskList;
		}
		
		XMLTaskReader xmlReader = new XMLTaskReader();

		try {
			taskList = xmlReader.read(AppConstants.appXmlPath);
			return taskList;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void updatePath(String path){
		browseFilePathLabel.setText(path);
		browseFilePathLabel.setToolTipText(path);
		
		getTaskVO().get(0).setExcelFilePath(path);
		
	}
	@Override
	public void updateProgressText(String progress){
		progressLabel.setText(progress);
	}
	@Override
	public void updateProgressUI(int percentageComplete){
		
//		try {
//			Thread.currentThread().join();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		
		synchronized (lock) {
			for( int start = jp.getValue(); start<=percentageComplete;start++){
				jp.setValue(start);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			}
			
		}
		
	}
	private Object lock = new Object();
}


class ExecuteTaskActionListener implements ActionListener {
	private StatusProgressListener statusProgressListener;
	public ExecuteTaskActionListener( StatusProgressListener registerUI ){
		this.statusProgressListener = registerUI;
	}
	AppMain main = new AppMain();
	@Override
	public void actionPerformed(ActionEvent e) {
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				main.init();
				main.registerExecuteTaskActionListener(ExecuteTaskActionListener.this);
				main.stepExcelToDB(((MainContainerPanel)statusProgressListener).getTaskVO().get(0));
			}
			
		}).start();
		
		

	}
	
	public void updateProgressUI(final int i){
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
				statusProgressListener.updateProgressUI(i);
//			}}).start();
		
	}
	
	public void updateupdateProgressText(final String progress){
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
				statusProgressListener.updateProgressText(progress);
//			}}).start();
		
	}
	
}

class FileChooserActionListener implements ActionListener {
	private MainContainerPanel mainContainerObj;
	public FileChooserActionListener( MainContainerPanel registerUI ){
		this.mainContainerObj = registerUI;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir", "."));

		FileFilter ff = new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;

				} else if (f.getName().toLowerCase().endsWith("xls")
						|| f.getName().toLowerCase().endsWith("xlsx")) {
					return true;
				} else{
					return false;
				}
			}

			public String getDescription() {
				return "Excel file only";
			}
		};

		jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
		jfc.setFileFilter(ff);

		if (jfc.showDialog(null, "Choose file") == JFileChooser.APPROVE_OPTION) {
			String fileName = jfc.getSelectedFile().getPath();
			System.out.println(fileName);
			
			if(mainContainerObj != null){
				mainContainerObj.updatePath(fileName);
			}
			
		}
	}
}
