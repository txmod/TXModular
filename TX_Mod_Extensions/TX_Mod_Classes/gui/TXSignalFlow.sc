// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSignalFlow {

classvar	<>system;	    			// parent system - set by parent
classvar modulesVisibleOrigin;
classvar layoutWidth;
classvar layoutHeight;

*initClass{
	modulesVisibleOrigin = Point.new(0,0);
	layoutWidth = 1600;
	layoutHeight = 1600;
}

*saveData {
	^[[modulesVisibleOrigin.x,modulesVisibleOrigin.y], layoutWidth, layoutHeight];
}

*loadData { arg arrData;
	modulesVisibleOrigin.x = arrData[0][0] ? 0;
	modulesVisibleOrigin.y = arrData[0][1] ? 0;
	layoutWidth = arrData[1] ? 1600;
	layoutHeight = arrData[2] ? 1600;
}

*setPosition{ arg module, rows = 1;
	var layoutPos;
	//	set position
	layoutPos = this.nextFreePosition(rows);
	module.posX = layoutPos.x;
	module.posY = layoutPos.y;
	this.sizeAdjustBeyond(layoutPos);
}

*setPositionNear{ arg module, otherModule, rows = 1;
	var layoutPos;
	//	set position
	layoutPos = this.nextFreePositionNear(otherModule, rows);
	module.posX = layoutPos.x;
	module.posY = layoutPos.y;
	this.sizeAdjustBeyond(layoutPos);
}

*sizeAdjustBeyond { arg layoutPos, argAction;
	var changed = false;
	if ((layoutWidth - 300) < layoutPos.x, {
		layoutWidth = layoutWidth + 300;
		changed = true;
	});
	if ((layoutHeight - 100) < layoutPos.y, {
		layoutHeight = layoutHeight+100;
		changed = true;
	});
	if (changed and: argAction.notNil, {
		argAction.value;
	});

}

*nextFreePosition { arg rows = 1;
	var arrAllModules;
	arrAllModules = system.arrSystemModules ++ system.arrAllBusses ++ TXChannelRouting.arrChannels;
	(50, 150 ..8000).do({ arg yVal;
		[ 0, 150, 300, 450, 600 ].do({ arg xVal;
			if (this.viewIntersecting(Rect(xVal, yVal-10, 130, rows * 50), arrAllModules).isNil, {
				^Point(xVal, yVal);
			});
		});
	});
	^Point(10, 10);
}

*nextFreePositionNear {arg module, rows = 1;
	var arrAllModules, holdX, holdY;
	arrAllModules = system.arrSystemModules ++ system.arrAllBusses ++ TXChannelRouting.arrChannels;
	(50, 100 ..4000).do({ arg yVal;
		[ 0, 150, 300, 450, 600 ].do({ arg xVal;
			holdX = xVal + module.posX;
			holdY = yVal + module.posY;
			if (this.viewIntersecting(Rect(holdX, holdY-5, 130, rows * 50), arrAllModules).isNil, {
				^Point(holdX, holdY);
			});
		});
	});
	^Point(10, 10);
}

*clearPositionData{
	var arrAllModules;
	arrAllModules = system.arrSystemModules
		++ system.arrAllBusses
		++ TXChannelRouting.arrChannels;
	arrAllModules.do ({arg module, i;
		module.posX = 0;
		module.posY = 0;
	});
}

*rebuildPositionData{
	var arrChannels;
	// reset layout positions of all modules in channels
	arrChannels = TXChannelRouting.arrChannels;
	arrChannels.do ({arg argChannel, i;
		var arrModules, numModules;
		// create var
		arrModules = [argChannel.sourceModule, argChannel, argChannel.insert1Module,
			argChannel.insert2Module, argChannel.insert3Module,
			argChannel.insert4Module, argChannel.insert5Module
		];
		arrModules = arrModules.reject({arg item; item.isNil});
		numModules = arrModules.size;
		// position source
		if ((argChannel.sourceModule.posX == 0) and: (argChannel.sourceModule.posY == 0), {
			// set position
			this.setPosition(argChannel.sourceModule, numModules);
		});
		// position inserts
		(arrModules.size - 1).do({arg i;
			if ((arrModules[i+1].posX == 0) and: (arrModules[i+1].posY == 0), {
				// set position
				this.setPositionNear(arrModules[i+1], arrModules[i], numModules - 1 - i);
			});
		});
	});
	// reset layout positions any remaining modules
	system.arrSystemModules.do ({arg module, i;
		if ((module.posX == 0) and: (module.posY == 0), {
			// set position
			this.setPosition(module);
		});
	});
}

*viewIntersecting { |argRect, argAllModules|
	argAllModules.do({ |view|
		var viewRect;
		viewRect = Rect(view.posX, view.posY, 130, 22);
		if (viewRect.intersects(argRect),
			{ ^view });
	});
	^nil
}

*makeGui{ arg parent;
	var layoutsScrollView, layoutBox, modLayoutView;
	var modListBox, listModules, listViewModules, displayModule;
	var arrAllSourceActionModules, arrAllSourceActionModNames, btnRebuildLayout;

	// create array of names of all system's source, insert & action modules.
	arrAllSourceActionModules = system.arrSystemModules
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "action")
			or:
			(item.class.moduleType == "insert") ;
		 })
		.sort({ arg a, b;
			a.instName < b.instName
		});
	arrAllSourceActionModNames = arrAllSourceActionModules.collect({arg item, i;  item.instName; });


// list of all modules	- REMOVED FOR NOW
//	modListBox =  CompositeView(parent,Rect(0,0, 153, 570));
//	modListBox.background = TXColour.sysChannelAudio;
//	modListBox.decorator = FlowLayout(modListBox.bounds);
//	listModules = [ "    System Modules"] ++ arrAllSourceActionModNames;
//	listViewModules = ListView(modListBox, 145 @ 562)
//			.items_(listModules)
//			.background_(TXColor.white)
//			.stringColor_(TXColor.sysGuiCol1)
//			.hiliteColor_(TXColor.sysGuiCol1)
//			.action = {|view|
//				// set variable
//				if (view.value > 0, {
//					modLayoutView.unhighlightAllViews;
//					arrAllSourceActionModules.at(view.value-1).highlight = true;
//					modLayoutView.userView.refresh;
//					modLayoutView.userView.focus(true);
//					view.value = 0;
//				});
//			};

	// make ScrollView
	layoutsScrollView = ScrollView(parent, Rect(0, 0, 1200, 600)).hasBorder_(false);
	if (GUI.current.asSymbol == \cocoa, {
		layoutsScrollView.autoScrolls_(false);
	});
	layoutsScrollView.action = {arg view; modulesVisibleOrigin = view.visibleOrigin; };
	layoutBox = CompositeView(layoutsScrollView, Rect(0,0, layoutWidth, layoutHeight));
	layoutBox.background = TXColor.sysModuleWindow;
	layoutsScrollView.visibleOrigin = modulesVisibleOrigin;
//	layoutBox.decorator = FlowLayout(layoutBox.bounds);
//	layoutBox.decorator.margin.x = 0;
//	layoutBox.decorator.margin.y = 0;
//	layoutBox.decorator.reset;
	modLayoutView = TXSignalFlowView(layoutBox, Rect(0,0, layoutWidth, layoutHeight), system);
	// button - Rebuild Layout
	btnRebuildLayout = Button(parent, 140 @ 27);
	btnRebuildLayout.states = [["Auto Rebuild Layout", TXColor.white, TXColor.sysDeleteCol]];
	btnRebuildLayout.action = {
		// confirm before action
		TXInfoScreen.newConfirmWindow(
			{
				// clear layout positions of all modules
				this.clearPositionData;
				// reset layout positions of all busses
				system.initBusPositions;
				// Then rest channels and modules
				this.rebuildPositionData;
//				system.showView;
			},
			"Are you sure you want to rebuild the layout based on channels order?"
		);
	};
	}

}

