package thesis.actions;

import java.util.Random;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

import thesis.Activator;
import thesis.data.Mutant;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class GenerateMutantsAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
//	public GenerateMutantsAction() {}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		Activator.getDefault();
		//TODO replace this section with code that uses MuJava to generate mutants
//		MessageDialog.openInformation(
//			window.getShell(),
//			"Thesis",
//			"Code needs to be added here for generating the mutants.\n" +
//			"This could be done with many other tools such as MuJava or ConMAn");
		Activator.mutantList.clear();
		Activator.testList.clear();
		
		//Generate some fake results
		Random rand=new Random();
		for(int x=0;x<5;x++){
			Mutant e=new Mutant("Foo", rand.nextInt(1000), "Bar");
			Activator.mutantList.add(e);
		}
		
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}