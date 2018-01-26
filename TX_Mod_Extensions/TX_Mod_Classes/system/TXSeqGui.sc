// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSeqGui {

classvar	<>system;	    			// parent system - set by parent
classvar <>displayModule; 		// shows current module who's gui will be shown
classvar popNewModuleInd;


////////////////////////////////////////////////////////////////////////////////////

*initClass{
	displayModule = nil;
	popNewModuleInd = 0;
}

*makeGui{ arg parent;
	var arrAllPossCurSeqClasses, arrAllPossSeqNames, arrAllSeqModules, arrAllSeqModNames;
	var popNewModule, btnAddModule, popDisplayMod;

	// create array of names of all possible seq modules.
	arrAllPossCurSeqClasses = system.arrAllPossCurSeqModules;
	arrAllPossSeqNames = arrAllPossCurSeqClasses.collect({ arg item, i; item.defaultName; });
	// create array of names of all system's seq modules.
	arrAllSeqModules = system.arrSystemModules
		.select({ arg item, i;
			arrAllPossCurSeqClasses.indexOf(item.class).notNil;
		 })
		.sort({ arg a, b;
			a.instName < b.instName
		});
	arrAllSeqModNames = arrAllSeqModules.collect({arg item, i;  item.instName; });
	// popup - display module selector
	popDisplayMod = PopUpMenu(parent, 250 @ 24).background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
	popDisplayMod.items = ([ " "] ++ arrAllSeqModNames).collect({ arg item, i; "Display: " ++ item;});
	popDisplayMod.action = {|view|
		// set variable
		if (view.value == 0, {
			displayModule = nil;
		}, {
			displayModule = arrAllSeqModules.at(view.value-1)
		});
		// update view
		system.showView;
	};
	popDisplayMod.value = ((arrAllSeqModules.indexOf(displayModule) ? -1) + 1);
	// spacer
	StaticText(parent, 30 @ 20);

	// button - Add new Source module
	btnAddModule = Button(parent, 200 @ 24);
	btnAddModule.states = [["Add new sequencer module:", TXColor.white, TXColor.sysGuiCol1]];
	btnAddModule.action = {
		var newModuleClass, newModule;
		// set new module class
		newModuleClass = arrAllPossCurSeqClasses.at(popNewModule.value);
		// ask system to add new module
		newModule = system.addModule(newModuleClass);
		// reset variable
		displayModule = newModule;
		// update view
		system.showView;
	};
	// popup - new module
	popNewModule = PopUpMenu(parent, 210 @ 24).background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
	popNewModule.items = arrAllPossSeqNames;
	popNewModule.action = {|view|
		// store current data
		popNewModuleInd = view.value;
	};
	popNewModule.value = popNewModuleInd ? 0;

	// spacer line
	parent.decorator.nextLine;
	// display module gui
	if (displayModule.notNil, {
		displayModule.openGui(parent);
	});
}

}


