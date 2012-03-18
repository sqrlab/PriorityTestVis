package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import thesis.Activator;
import thesis.data.TestResult;

public class Timeline extends ViewPart{
	public static final String ID = "testview.views.SampleView";
	public static ArrayList<TestResult> testData,selectedList,nonSelectedList;
	private static Canvas canvasSelected,canvasUnselected;
	private static GC gcSelected,gcUnselected;
	private static Group selectionHolder;
	private static Group poolHolder;
	private final static int kTotal_time=Activator.TimeGoal;
	
	private static ArrayList<Integer> previously_detected_mutants=new ArrayList<Integer>();
	
	private static TimelinePainterSelectedTests tlPainterSelected;
	private static TimelinePainterTestPool tlPainterUnSelected;
	private static TimelineMouseHover tlMouseHoverSelected;
	private static TimelineMouseHover tlMouseHoverPool;
	private static TimelineMouseClicker tlMouseClicker;
	
	
	public static void update(ArrayList<TestResult> tests){
		testData=tests;
		nonSelectedList=testData;
		selectedList.clear();
		selectTestsToAddToSet();
		updateGraphics();
	}
	
	public static void update(){
		updateGraphics();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		FillLayout fill=new FillLayout();
		fill.type=SWT.VERTICAL;
		parent.setLayout(fill);
		selectionHolder=new Group(parent, SWT.SHADOW_NONE);
		selectionHolder.setText("Selected Tests");
		selectionHolder.setLayout(new FillLayout());
		canvasSelected=new Canvas(selectionHolder,SWT.NONE);
		gcSelected=new GC(canvasSelected);
		poolHolder=new Group(parent, SWT.SHADOW_NONE);
		poolHolder.setText("Test Pool");
		poolHolder.setLayout(new FillLayout());
		canvasUnselected=new Canvas(poolHolder, SWT.NONE|SWT.H_SCROLL);
//		final ScrollBar hBarUnSelected=canvasUnselected.getHorizontalBar();
		gcUnselected=new GC(canvasUnselected);
		
		testData=new ArrayList<TestResult>();
		selectedList=new ArrayList<TestResult>();
		nonSelectedList=new ArrayList<TestResult>();
		
		tlPainterSelected=new TimelinePainterSelectedTests(canvasSelected,selectedList);
		canvasSelected.addPaintListener(tlPainterSelected);
		tlMouseHoverSelected=new TimelineMouseHover(selectedList,false);
		canvasSelected.addMouseMoveListener(tlMouseHoverSelected);
		
		tlMouseHoverPool=new TimelineMouseHover(nonSelectedList,true);
		canvasUnselected.addMouseMoveListener(tlMouseHoverPool);
//		System.out.println(parent.toString()+" "+parent.getBounds().width+":"+parent.getBounds().height);
		
		tlMouseClicker=new TimelineMouseClicker();
		canvasSelected.addMouseListener(tlMouseClicker);
		
		tlPainterUnSelected=new TimelinePainterTestPool(canvasUnselected, nonSelectedList);
		canvasUnselected.addPaintListener(tlPainterUnSelected);
	}
	

	private static void updateGraphics(){
		tlPainterSelected.drawGraphics(gcSelected);
		tlPainterUnSelected.drawGraphics(gcUnselected);
	}
	
	public void setFocus() {
		selectionHolder.setFocus();
		poolHolder.setFocus();
	}
	
	
	
	private static void addTestToSet(TestResult testToAdd){
		nonSelectedList.remove(testToAdd);
		
		//Remove partial uniqueness
		testToAdd.removeTrueUniqueness(previously_detected_mutants);
		testToAdd.removeUniqueness(previously_detected_mutants);
		previously_detected_mutants.addAll(testToAdd.getDetectedMutants());
		
		//Remove true uniqueness
		for(int i=0;i<selectedList.size();i++){
			selectedList.get(i).removeTrueUniqueness(testToAdd.getDetectedMutants());
		}
		
		selectedList.add(testToAdd);
		tlPainterSelected.update(selectedList);
		tlPainterUnSelected.update(nonSelectedList);
		tlMouseHoverSelected.update(selectedList);
		tlMouseHoverPool.update(nonSelectedList);
	}
	
	private void removeTestFromSet(TestResult testToRemove){
		selectedList.remove(testToRemove);
		nonSelectedList.add(testToRemove);
	}
	
	private static void selectTestsToAddToSet(){
		int currentTime=0;
		if(nonSelectedList.size()>0){
			while(currentTime<kTotal_time){
				if(nonSelectedList.size()==0){
					break;
				}else{
					currentTime+=nonSelectedList.get(0).getTime();
					addTestToSet(nonSelectedList.get(0));
				}
			}
		}
		tlPainterUnSelected.update(nonSelectedList);
	}

	public static int getUnselectedWidth(){
		return Timeline.canvasUnselected.getClientArea().width;
	}

}
