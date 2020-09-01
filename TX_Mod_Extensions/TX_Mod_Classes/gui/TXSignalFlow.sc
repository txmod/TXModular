// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSignalFlow {

	classvar <>system;	    			// parent system - set by parent
	classvar <>classData;

	*initClass{
		classData = ();
		classData.modulesVisibleOrigin = Point.new(0,0);
		classData.maxSigFlowBoxWidth = 2000;
		classData.layoutWidth = 1200;
		classData.layoutHeight = 1200;
		classData.sigFlowScale = 1;
	}

	*layoutWidth {
		^classData.layoutWidth;
	}

	*layoutHeight {
		^classData.layoutHeight;
	}

	*modulesVisibleOrigin {
		^classData.modulesVisibleOrigin;
	}

	*modulesVisibleOrigin_ {arg argOrigin;
		classData.modulesVisibleOrigin = argOrigin;
	}

	*saveData {
		^[[classData.modulesVisibleOrigin.x, classData.modulesVisibleOrigin.y],
			classData.layoutWidth,
			classData.layoutHeight,
			classData.sigFlowScale,
		];
	}

	*loadData { arg arrData;
		classData.modulesVisibleOrigin.x = arrData[0][0] ? 0;
		classData.modulesVisibleOrigin.y = arrData[0][1] ? 0;
		classData.layoutWidth = arrData[1] ? 1200;
		classData.layoutHeight = arrData[2] ? 1200;
		classData.sigFlowScale = arrData[3] ? 1;
	}

	*setPosition{ arg module, rows = 1;
		var layoutPos;
		//	set position
		layoutPos = this.nextFreePosition(rows);
		module.posX = layoutPos.x;
		module.posY = layoutPos.y;
		this.adjustSigFlowSizeBeyond(layoutPos);
	}

	*setPositionNear{ arg module, otherModule, rows = 1;
		var layoutPos;
		//	set position
		layoutPos = this.nextFreePositionNear(otherModule, rows);
		module.posX = layoutPos.x;
		module.posY = layoutPos.y;
		this.adjustSigFlowSizeBeyond(layoutPos);
	}

	*adjustSigFlowSizeBeyond { arg layoutPos, argAction;
		var changed = false;
		if ((classData.layoutWidth - 300) < layoutPos.x, {
			classData.layoutWidth = classData.layoutWidth + 300;
			changed = true;
		});
		if ((classData.layoutHeight - 100) < layoutPos.y, {
			classData.layoutHeight = classData.layoutHeight+100;
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

	*makeGui{ arg parent, scrollWidth = 1200, scrollHeight = 600;
		var modListBox, listModules, listViewModules, displayModule;
		var arrAllSourceActionModules, arrAllSourceActionModNames, btnRebuildLayout, zoomSlider;

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

		classData.sigFlowBox = CompositeView(parent, Rect(0,0, classData.maxSigFlowBoxWidth, scrollHeight));
		classData.sigFlowBox.background = TXColor.sysModuleWindow;
		classData.sigFlowBox.decorator = FlowLayout(classData.sigFlowBox.bounds);
		classData.sigFlowBox.decorator.margin.x = 0;
		classData.sigFlowBox.decorator.margin.y = 0;
		classData.sigFlowBox.decorator.reset;
		classData.sigFlowBox.decorator.shift(2, 2);

		zoomSlider = TXSlider(classData.sigFlowBox, 240 @ 20, "Zoom", ControlSpec(0.3, 1, default: 1),
			{arg view;  classData.sigFlowScale = view.value; classData.modLayoutView.userView.scale = view.value},
			labelWidth: 50, numberWidth: 40);
		zoomSlider.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		zoomSlider.sliderView.background_(TXColour.sysGuiCol1);
		{zoomSlider.valueAction = classData.sigFlowScale;}.defer(0.02);

		classData.sigFlowBox.decorator.shift(20, 0);

		StaticText(classData.sigFlowBox, 250 @ 20)
		.align_('center')
		.string_("Press ctrl & drag mouse to move viewing box")
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);

		classData.sigFlowBox.decorator.shift(20, 0);

		btnRebuildLayout = Button(classData.sigFlowBox, 210 @ 20);
		btnRebuildLayout.states = [["Auto rebuild layout in channel order",
			TXColor.white, TXColor.sysDeleteCol]];
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
					system.showView;
				},
				"Are you sure you want to rebuild the layout based on channels order?"
			);
		};

		classData.sigFlowBox.decorator.nextLine;

		// make ScrollView
		classData.layoutsScrollView = ScrollView(classData.sigFlowBox,
			Rect(0, 0, scrollWidth, scrollHeight - 26)).hasBorder_(false);
		if (GUI.current.asSymbol == \cocoa, {
			classData.layoutsScrollView.autoScrolls_(false);
		});
		classData.layoutsScrollView.action = {arg view; classData.modulesVisibleOrigin = view.visibleOrigin; };
		{classData.layoutsScrollView.visibleOrigin = classData.modulesVisibleOrigin}.defer(0.05);

		classData.layoutBox = CompositeView(classData.layoutsScrollView,
			Rect(0,0, classData.layoutWidth, classData.layoutHeight));
		classData.layoutBox.background = TXColor.sysModuleWindow;
		classData.layoutBox.decorator = FlowLayout(classData.layoutBox.bounds);
		classData.layoutBox.decorator.margin.x = 0;
		classData.layoutBox.decorator.margin.y = 0;
		classData.layoutBox.decorator.reset;

		// Signal flow view
		classData.modLayoutView = TXSignalFlowView(classData.layoutBox,
			Rect(0, 26, classData.layoutWidth, classData.layoutHeight),
			system, this);
	}

}

