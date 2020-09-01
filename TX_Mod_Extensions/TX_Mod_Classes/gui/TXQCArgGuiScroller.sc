// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
		
TXQCArgGuiScroller {	

var <>arrControls, <>scrollView, controlCount, system, holdBackgroundBox, holdArrGuis;
	
	*new { arg argSystem, argParent, dimensions, argModule, argArrLabels, argArrNumSynthArgNames,
			argArrActiveNumSynthArgNames, argArrModuleArgs, argSetArgValFunc, scrollViewAction;
		^super.new.init(argSystem, argParent, dimensions, argModule, argArrLabels, argArrNumSynthArgNames,
			argArrActiveNumSynthArgNames, argArrModuleArgs, argSetArgValFunc, scrollViewAction);
	}
	init { arg argSystem, argParent, dimensions, argModule, argArrLabels, argArrNumSynthArgNames,
			argArrActiveNumSynthArgNames, argArrModuleArgs, argSetArgValFunc, scrollViewAction;
			
		var holdView, scrollBox;
		
		system = argSystem;

  		// reset variables
		controlCount = argArrLabels.size;
		// add ScrollView
		scrollView = ScrollView(argParent, Rect(0, 0, dimensions.x, dimensions.y)) 
			.hasBorder_(false);
		if (GUI.current.asSymbol == \cocoa, {
			scrollView.autoScrolls_(false);
		});
		scrollView.action = scrollViewAction;
		scrollView.hasHorizontalScroller = false;
		scrollView.hasVerticalScroller = true;
		scrollBox = CompositeView(scrollView, Rect(0, 0, dimensions.x, 20 + (controlCount * 56)));
		scrollBox.decorator = FlowLayout(scrollBox.bounds);
		scrollBox.decorator.margin.x = 0;
		scrollBox.decorator.margin.y = 0;
		scrollBox.decorator.reset;

		// display controls  
	controlCount.do({ arg item, i;
		var label, getNumFunc, setNumFunc, setActiveFunc, arrArgs, arrValsFunc, setArgValFunc; 
		scrollBox.decorator.nextLine;
		label = argArrLabels[i].asString;
		getNumFunc = {
			argModule.getSynthArgSpec(argArrNumSynthArgNames[i]);
		};
		setNumFunc = {arg argVal; 
			argModule.setSynthValue(argArrNumSynthArgNames[i], argVal);
		};
		setActiveFunc = {arg argVal; 
			argModule.setSynthValue(argArrActiveNumSynthArgNames[i], argVal);
			// rebuild synth and activate osc
			{argModule.rebuildSynth;}.defer(0.2);
			{argModule.oscActivate;}.defer(0.2);
		};
		arrArgs = argArrModuleArgs;
		setArgValFunc = {argSetArgValFunc.value(i);};
	//  arrQCArgData = [0, "", 0, 1, 0.5, 0.5, 0.5, 1].dup(maxParameters);   
	//  array of :  argDataType, argStringVal, argMin, argMax, argHue, argSaturation, argBrightness, argAlpha
	//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(HSBA), 
	//	6.Directory Name, 7.File Name], 
		arrValsFunc = {
			var holdArgs, holdMin, holdMax, holdNum;
			holdArgs = arrArgs[i];
			holdMin = holdArgs.at(2);
			holdMax = holdArgs.at(3);
			holdNum = argModule.getSynthArgSpec(argArrNumSynthArgNames[i]) ? 0;
			holdArgs ++ (holdMin + (holdNum * (holdMax - holdMin)));
		};
		holdView = TXQCArgGui(scrollBox, dimensions.x @ 20, label, getNumFunc, setNumFunc, setActiveFunc,  
			arrArgs, i, setArgValFunc, 200, 60, 450);
		holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.label2View.stringColor_(TXColour.sysGuiCol1).background_(TXColor.grey(0.85));
		holdView.popupMenuView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.presetPopup.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.valueAll_(arrValsFunc.value);
		holdArrGuis = holdArrGuis.add(holdView);

	}); // end of controlCount.do

		// draw final line
		scrollBox.decorator.nextLine;
		scrollBox.decorator.shift(0,2);
		StaticText(scrollBox, 700 @ 1).background_(TXColor.sysGuiCol2);
		scrollBox.decorator.nextLine;
		scrollBox.decorator.shift(0,2);
		// dummy text as spacer
		StaticText(scrollBox, Rect(0, 0, 20, 20));
	}
	
update { arg arrArgs, argArrNumSynthArgNames, argModule;
	holdArrGuis.do({ arg item, i;
		var arrValsFunc;
		arrValsFunc = {
			var holdArgs, holdMin, holdMax, holdNum;
			holdArgs = arrArgs[i];
			holdMin = holdArgs.at(2);
			holdMax = holdArgs.at(3);
			holdNum = argModule.getSynthArgSpec(argArrNumSynthArgNames[i]) ? 0;
			holdArgs ++ (holdMin + (holdNum * (holdMax - holdMin)));
		};
		item.valueAll_(arrValsFunc.value);
	});
}

}
