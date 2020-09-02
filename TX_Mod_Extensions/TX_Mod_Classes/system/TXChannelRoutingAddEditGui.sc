// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChannelRoutingAddEditGui { //  Gui to add or edit a module or channel

	*getBoxWidth {arg dataBank, localData;
		var holdWidth;
		if (localData.displayMode == 'modulesMode', {
			holdWidth = dataBank.allModulesBoxWidth;
		}, {
			holdWidth = dataBank.channelTitleBoxWidth;
		});
			^holdWidth;
		}

	*makeGui{ arg parent, system, dataBank, arrAllSourceActionModules, arrAllSourceActionModNames,
		arrAllSourceModsBusses, arrAllSourceModBusNames, localData;
		//var highlightCol = TXColor.sysGuiCol1.blend(TXColor.red, 0.03).blend(TXColor.white, 0.35);
		var arrViews, holdView, holdPosX, holdPosY;
		var arrMenuNames, arrMenuWidths;

		arrViews = [];

		if (localData.displayMode == 'modulesMode', {
			arrMenuNames = ["+Source", "+Meter", "Edit Module"];
			arrMenuWidths = [50, 48, 70,];
		}, {
			arrMenuNames = ["Titles", "+Channel", "+Insert",];
			arrMenuWidths = [36, 58, 46,];
		});

		// select buttons
		arrMenuNames.do({arg item, i;
			var stringColor, holdView;
			if (localData.addEditSelection == item, {
				stringColor = TXColor.paleYellow2;
			},{
				stringColor = TXColor.white;
			});
			holdView = StaticText(parent, arrMenuWidths[i] @ 20);
			holdView.align_(\center);
			holdView.string = item;
			holdView.stringColor_(stringColor).background_(TXColor.sysGuiCol1);
			holdView.mouseDownAction = {
				localData.addEditSelection = item;
				if (localData.displayMode == 'modulesMode', {
					dataBank.allModulesVisibleOrigin = Point(0,0);
				},{
					dataBank.channelTitleVisibleOrigin = Point(0,0);
				});
				localData.headingView.string = this.getHeadingString(localData.addEditSelection);
				this.adjustVisibility(localData);
				{this.rebuildGui(parent, system, dataBank,
					arrAllSourceActionModules, arrAllSourceActionModNames,
					arrAllSourceModsBusses, arrAllSourceModBusNames, localData)}.defer;
				// reset colors
				arrViews.do({arg view, viewInd;
					var stringColor;
					if (viewInd == i, {
						stringColor = TXColor.paleYellow2;
					},{
						stringColor = TXColor.white;
					});
					view.stringColor = stringColor;
				});
			};
			arrViews = arrViews.add(holdView);
		});
		parent.decorator.nextLine;

		holdPosX = parent.decorator.left;
		holdPosY = parent.decorator.top;

		parent.decorator.nextLine;
		// restore position
		parent.decorator.left = holdPosX;
		parent.decorator.top = holdPosY;

		// buttons - open/close all categories
		localData.menuOpenBtn = Button(parent, 10 @ 20);
		localData.menuOpenBtn.states = [["v", TXColor.white, TXColor.sysGuiCol1]];
		localData.menuOpenBtn.action = {
			this.openAllCategories(localData);
			system.showView;
		};
		localData.menuCloseBtn = Button(parent, 10 @ 20);
		localData.menuCloseBtn.states = [[">", TXColor.white, TXColor.sysGuiCol1]];
		localData.menuCloseBtn.action = {
			this.closeAllCategories(localData);
			if (localData.displayMode == 'modulesMode', {
				dataBank.allModulesVisibleOrigin = Point(0,0);
			},{
				dataBank.channelTitleVisibleOrigin = Point(0,0);
			});
			system.showView;
		};
		// Heading
		localData.headingView = StaticText(parent, Rect(0,0, 144, 20));
		localData.headingView.stringColor_(TXColor.white).background_(TXColor.sysLabelBackground);
		localData.headingView.align_(\center);
		localData.headingView.string = this.getHeadingString(localData.addEditSelection);

		parent.decorator.nextLine;

		// Line
		localData.channelTitlesLine = StaticText(parent, Rect(0,0, 186, 2)).background_(TXColor.white);

		parent.decorator.nextLine;

		if (localData.displayMode == 'modulesMode', {
			localData.allModulesScrollView = ScrollView(parent,
				Rect(0,0, dataBank.allModulesBoxWidth, dataBank.boxHeight - 58))
			.hasBorder_(false).resize_(4);
			localData.allModulesScrollView.background = TXColor.sysMainWindow;
			localData.allModulesScrollView.action = {arg view; dataBank.allModulesVisibleOrigin = view.visibleOrigin; };
		},{
			localData.channelTitleScrollView = ScrollView(parent,
				Rect(0,0, dataBank.channelTitleBoxWidth, dataBank.boxHeight - 58))
			.hasBorder_(false).resize_(4);
			localData.channelTitleScrollView.background = TXColor.sysMainWindow;
			localData.channelTitleScrollView.action = {arg view; dataBank.channelTitleVisibleOrigin = view.visibleOrigin; };
		});
		this.adjustVisibility(localData);

		this.rebuildGui(parent, system, dataBank, arrAllSourceActionModules, arrAllSourceActionModNames,
			arrAllSourceModsBusses, arrAllSourceModBusNames, localData);
	}

	*adjustVisibility {arg localData;
		if (localData.addEditSelection == "Titles", {
			localData.menuOpenBtn.visible = false;
			localData.menuCloseBtn.visible = false;
			// localData.headingView.visible = false;
			localData.channelTitlesLine.visible = false;
		}, {
			localData.menuOpenBtn.visible = true;
			localData.menuCloseBtn.visible = true;
			// localData.headingView.visible = true;
			localData.channelTitlesLine.visible = true;
		});
	}

	*getHeadingString {arg selection;
		var headingString, holdChanCount, holdAudioCount, holdControlCount;
		if (selection == "Titles", {
			holdChanCount = TXChannelRouting.arrChannels.size;
			holdAudioCount = 0;
			holdControlCount = 0;
			TXChannelRouting.arrChannels.do({arg item, i;
				if (item.channelRate == "audio", {
					holdAudioCount = holdAudioCount + 1;
				}, {
					holdControlCount = holdControlCount + 1;
				});
			});
			headingString = "Channels: " ++ holdChanCount.asString
				++ "  A: " ++ holdAudioCount.asString ++ "  C: " ++ holdControlCount.asString;
		});
		if (selection == "+Source", {
			headingString = "add source module";
		});
		if (selection == "+Insert", {
			headingString = "drag to add insert module";
		});
		if (selection == "+Channel", {
			headingString = "add channel from source";
		});
		if (selection == "+Meter", {
			headingString = "add meter from source";
		});
		if (selection == "Edit Module", {
			headingString = "edit or delete module";
		});
		^headingString;
	}

	*showChannelTitles {arg parent, width, height;
		TXChannel.makeTitleGui(parent, width, height);
	}

	*rebuildGui{ arg parent, system, dataBank, arrAllSourceActionModules, arrAllSourceActionModNames,
		arrAllSourceModsBusses, arrAllSourceModBusNames, localData;
		// build gui
		var holdView, holdMaxHeight, holdCategory, holdCategoryCount, holdLastCat;
		var arrSourceModules, arrInsertModules, arrChannelSources, arrMeterSources;
		var arrEditModules, arrEditModuleNames, busFound;
		var arrModulesForMeters, arrAudioAuxMonoBusses, moduleCatString;
		//var highlightCol = TXColor.sysGuiCol1.blend(TXColor.red, 0.03).blend(TXColor.white, 0.35);

		// init - build arrays & set height
		localData.allCategories = [];
		if (localData.addEditSelection == "+Source", {
			arrSourceModules = [];
			holdCategory = nil;
			system.dataBank.arrSourceModulesByCategoryWithAlpha.do({arg item, i;
				if (holdCategory != item[0], {
					arrSourceModules = arrSourceModules.add([">" ++ item[0], nil]);
					holdCategory = item[0];
					localData.allCategories = localData.allCategories.add(item[0]);
				});
				arrSourceModules = arrSourceModules.add([" " ++ item[1], item[2]]);
			});
			arrSourceModules = this.adjustClosedCategories(arrSourceModules, dataBank, localData,
				localData.closedSourceCats);
			holdMaxHeight = 72 + ((arrSourceModules.size+1) * 24);
		});
		if (localData.addEditSelection == "+Channel", {
			localData.allCategories = localData.allCategories.add("Modules");
			localData.allCategories = localData.allCategories.add("Buses");
			busFound = false;
			arrChannelSources = [[">Modules", nil]];
			arrAllSourceModBusNames.do({arg item, i;
				if (arrAllSourceModsBusses[i].class.moduleType == "bus" && (busFound == false), {
					busFound = true;
					arrChannelSources = arrChannelSources ++ [[">Buses", nil]];
				});

				arrChannelSources = arrChannelSources ++ [[" " ++ item, arrAllSourceModsBusses[i]]];
			});
			arrChannelSources = this.adjustClosedCategories(arrChannelSources, dataBank, localData,
				localData.closedChannelCats);
			holdMaxHeight = 72 + ((arrChannelSources.size+1) * 24);
		});
		if (localData.addEditSelection == "+Insert", {
			arrInsertModules = [];
			system.dataBank.arrInsertModulesByCategoryWithAlpha.do({arg item, i;
				var holdString;
				if (item[0][0].asString == ">", {
					if (item[2] == "audio", {
						holdString = ">Audio: " ++ item[0].keep(1 - item[0].size);
					}, {
						if (item[2] == "control", {
							holdString = ">Control: " ++ item[0].keep(1 - item[0].size);
						}, {
							holdString = item[0];
						});
					});
					localData.allCategories = localData.allCategories.add(holdString.keep(1 - holdString.size));
				}, {
					holdString = item[0].keep(2 - item[0].size);
				});
				arrInsertModules = arrInsertModules.add([holdString, item[1]]);
			});
			arrInsertModules = this.adjustClosedCategories(arrInsertModules, dataBank, localData, localData.closedInsertCats);
			holdMaxHeight = 72 + ((arrInsertModules.size+1) * 24);
		});
		if (localData.addEditSelection == "+Meter", {
			localData.allCategories = localData.allCategories.add("Meters");
			localData.allCategories = localData.allCategories.add("Freqscope");
			localData.allCategories = localData.allCategories.add("Oscilloscope");

			arrModulesForMeters = system.arrSystemModules
			.select({arg item, i; item.class.noOutChannels > 0 ;})
			.sort({arg item1, item2; item2.instName > item1.instName;});
			arrMeterSources = [[">Meters", nil, nil]]
			++ arrModulesForMeters.collect({ arg item, i;
				var holdIndex, busArray, holdMeter;
				holdIndex = item.outBus.index;
				item.class.noOutChannels.do({arg item, i;
					busArray = busArray.add(holdIndex + i + 1);
				});
				[" " ++ item.instName, {
					holdMeter = TXMeterBridge.perform('addNewMeter', busArray-1, nil, nil, 10 @ 80,
						item.instName, item.class.moduleRate);
				}, item.class.moduleRate];
			})
			++ [[" Audio Out 1+2", [1,2]],
				[" Audio Out 3+4", [3,4]],
				[" Audio Out 5+6", [5,6]],
				[" Audio Out 7+8", [7,8]],
				[" Audio Out 9+10", [9,10]],
				[" Audio Out 11+12", [11,12]],
				[" Audio Out 13+14", [13,14]],
				[" Audio Out 15+16", [15,16]],
				[" Audio Out 1-4", (1..4)],
				[" Audio Out 1-8", (1..8)],
				[" Audio Out 1-16", (1..16)],
			].collect({ arg item, i;
				[item[0],  {
					var holdMeter = TXMeterBridge.perform('addOutputMeter', item[1]-1, nil, 10 @ 80, item[0]);
				}, "audio"];
			})
			++ [[" Audio In 1+2", [1,2]],
				[" Audio In 3+4", [3,4]],
				[" Audio In 5+6", [5,6]],
				[" Audio In 7+8", [7,8]],
				[" Audio In 1-4", (1..4)],
				[" Audio In 1-8", (1..8)],
			].collect({ arg item, i;
				[item[0],  {
					var holdMeter = TXMeterBridge.perform('addInputMeter', item[1]-1, nil, 10 @ 80, item[0]);
				}, "audio"];
			})
			++ (system.arrAudioAuxBusses ++ system.arrFXSendBusses)
			.collect({ arg item, i;
				[item.instName, {
					var holdMeter = TXMeterBridge.perform('addNewMeter', item.arrOutBusChoices.at(0).at(1), nil, nil,
						10 @ 80, item.instName, "audio");
				}, "audio"];
			})
			++ system.arrControlAuxBusses.collect({ arg item, i;
				[item.instName, {
					var holdMeter = TXMeterBridge.perform('addNewMeter', item.arrOutBusChoices.at(0).at(1), nil, nil,
						10 @ 80, item.instName, "control");
				}];
			}, "control")
			++ [[">Freqscope", nil, nil]]
			++ arrModulesForMeters.select({ arg item, i; item.class.moduleRate == "audio";}).collect({ arg item, i;
				var holdIndex, busArray, holdMeter;
				holdIndex = item.outBus.index;
				item.class.noOutChannels.do({arg item, i;
					busArray = busArray.add(holdIndex + i + 1);
				});
				[" " ++ item.instName,
					{this.openFreqScope(dataBank, item.instName, busArray-1)},
					item.class.moduleRate
				];
			})
			++ [
				" Audio Out 1", " Audio Out 2", " Audio Out 3",
				" Audio Out 4", " Audio Out 5", " Audio Out 6",
				" Audio Out 7", " Audio Out 8", " Audio Out 9",
				" Audio Out 10", " Audio Out 11", " Audio Out 12",
				" Audio Out 13", " Audio Out 14", " Audio Out 15", " Audio Out 16",
			]
			.collect({ arg item, i;
				[item, {this.openFreqScope(dataBank, item, i)}, "audio"];
			})
			++ [
				[" Audio Aux 1", system.arrAudioAuxBusses[0].arrOutBusChoices[0][1][0]],
				[" Audio Aux 2", system.arrAudioAuxBusses[0].arrOutBusChoices[0][1][1]],
				[" Audio Aux 3", system.arrAudioAuxBusses[1].arrOutBusChoices[0][1][0]],
				[" Audio Aux 4", system.arrAudioAuxBusses[1].arrOutBusChoices[0][1][1]],
				[" Audio Aux 5", system.arrAudioAuxBusses[2].arrOutBusChoices[0][1][0]],
				[" Audio Aux 6", system.arrAudioAuxBusses[2].arrOutBusChoices[0][1][1]],
				[" Audio Aux 7", system.arrAudioAuxBusses[3].arrOutBusChoices[0][1][0]],
				[" Audio Aux 8", system.arrAudioAuxBusses[3].arrOutBusChoices[0][1][1]],
				[" Audio Aux 9", system.arrAudioAuxBusses[4].arrOutBusChoices[0][1][0]],
				[" Audio Aux 10", system.arrAudioAuxBusses[4].arrOutBusChoices[0][1][1]],
			]
			.collect({ arg item, i;
				[item[0], {this.openFreqScope(dataBank, item[0], item[1])}, "audio"];
			})
			++ [[">Oscilloscope", nil, nil]]
			++ arrModulesForMeters.collect({ arg item, i;
				var holdIndex, busArray, holdMeter;
				holdIndex = item.outBus.index;
				item.class.noOutChannels.do({arg item, i;
					busArray = busArray.add(holdIndex + i + 1);
				});
				[" " ++ item.instName,
					{this.openOscilloscope(item.instName, busArray-1, item.class.moduleRate.asSymbol)},
					item.class.moduleRate
				];
			})
			++ [[" Audio Out 1+2", [1,2]],
				[" Audio Out 3+4", [3,4]],
				[" Audio Out 5+6", [5,6]],
				[" Audio Out 7+8", [7,8]],
				[" Audio Out 9+10", [9,10]],
				[" Audio Out 11+12", [11,12]],
				[" Audio Out 13+14", [13,14]],
				[" Audio Out 15+16", [15,16]],
			].collect({ arg item, i;
				[item[0], {this.openOscilloscope(item[0], item[1]-1, 'audio')}, "audio"];
			})
			++ [
				" Audio Out 1", " Audio Out 2", " Audio Out 3",
				" Audio Out 4", " Audio Out 5", " Audio Out 6",
				" Audio Out 7", " Audio Out 8", " Audio Out 9",
				" Audio Out 10", " Audio Out 11", " Audio Out 12",
				" Audio Out 13", " Audio Out 14", " Audio Out 15",
				" Audio Out 16",
			]
			.collect({ arg item, i;
				[item, {this.openOscilloscope(item, i, 'audio')}, "audio"];
			})
			++ [
				[" Audio Aux 1+2", system.arrAudioAuxBusses[0].arrOutBusChoices[0][1]],
				[" Audio Aux 3+4", system.arrAudioAuxBusses[1].arrOutBusChoices[0][1]],
				[" Audio Aux 5+6", system.arrAudioAuxBusses[2].arrOutBusChoices[0][1]],
				[" Audio Aux 7+8", system.arrAudioAuxBusses[3].arrOutBusChoices[0][1]],
				[" Audio Aux 9+10", system.arrAudioAuxBusses[4].arrOutBusChoices[0][1]],
				[" Audio Aux 1", system.arrAudioAuxBusses[0].arrOutBusChoices[0][1][0]],
				[" Audio Aux 2", system.arrAudioAuxBusses[0].arrOutBusChoices[0][1][1]],
				[" Audio Aux 3", system.arrAudioAuxBusses[1].arrOutBusChoices[0][1][0]],
				[" Audio Aux 4", system.arrAudioAuxBusses[1].arrOutBusChoices[0][1][1]],
				[" Audio Aux 5", system.arrAudioAuxBusses[2].arrOutBusChoices[0][1][0]],
				[" Audio Aux 6", system.arrAudioAuxBusses[2].arrOutBusChoices[0][1][1]],
				[" Audio Aux 7", system.arrAudioAuxBusses[3].arrOutBusChoices[0][1][0]],
				[" Audio Aux 8", system.arrAudioAuxBusses[3].arrOutBusChoices[0][1][1]],
				[" Audio Aux 9", system.arrAudioAuxBusses[4].arrOutBusChoices[0][1][0]],
				[" Audio Aux 10", system.arrAudioAuxBusses[4].arrOutBusChoices[0][1][1]],
			]
			.collect({ arg item, i;
				[item[0], {this.openOscilloscope(item[0], item[1], 'audio')}, "audio"];
			})
			// control aux oscilloscope
			++ system.arrControlAuxBusses.collect({ arg item, i;
				var name = " " ++ item.instName;
				[name,
					{this.openOscilloscope(item.instName,
						item.arrOutBusChoices.at(0).at(1), 'control')},
					"control"
				];
			})
			;
			arrMeterSources = this.adjustClosedCategories(arrMeterSources, dataBank, localData, localData.closedMeterCats);
			holdMaxHeight = 72 + ((arrMeterSources.size+1) * 24);
		});
		if (localData.addEditSelection == "Edit Module", {
			arrEditModules = [];
			arrEditModuleNames = [];
			holdCategory = nil;
			holdCategoryCount = 0;
			arrAllSourceActionModNames.do({arg item, i;
				var className = this.moduleNameRemoveNo(item);
				if (holdCategory != className, {
					holdCategory = className;
					holdCategoryCount = 0;
				},{
					holdCategoryCount = holdCategoryCount + 1;
					if (holdCategoryCount == 1, {
						// add category if more than 1 item in class
						arrEditModuleNames = arrEditModuleNames.add([">" ++ className]);
						arrEditModules = arrEditModules.add(nil); // dummy entry
						// swap last 2 entries of both
						arrEditModuleNames.swap(arrEditModuleNames.size - 1, arrEditModuleNames.size - 2);
						arrEditModules.swap(arrEditModuleNames.size - 1, arrEditModuleNames.size - 2);
						localData.allCategories = localData.allCategories.add(className);
					});
				});
				arrEditModuleNames = arrEditModuleNames.add([item]);
				arrEditModules = arrEditModules.add(arrAllSourceActionModules[i]);
			});
			arrEditModuleNames = this.adjustClosedCategories(arrEditModuleNames, dataBank, localData, localData.closedEditCats,
				alwaysAddFirstItem: true);
			//holdMaxHeight = 72 + ((arrAllSourceActionModNames.size+1) * 24);
			holdMaxHeight = 72 + ((arrEditModuleNames.size+1) * 24);
		});
		if (localData.addEditSelection == "Titles", {
			holdMaxHeight = 554;
		});

		if (localData.compBox.notNil, {
			localData.compBox.remove;
		});
		if (localData.displayMode == 'modulesMode', {
			localData.compBox = CompositeView(localData.allModulesScrollView, Rect(0,0, 184, holdMaxHeight));
		},{
			localData.compBox = CompositeView(localData.channelTitleScrollView, Rect(0,0, 184, holdMaxHeight));
		});
		localData.compBox.background = TXColor.sysMainWindow;
		localData.compBox.decorator = FlowLayout(localData.compBox.bounds);
		localData.compBox.decorator.margin.x = 0;
		localData.compBox.decorator.margin.y = 0;
		localData.compBox.decorator.gap.x = 3;
		localData.compBox.decorator.reset;

		if (localData.addEditSelection == "Titles", {
			this.showChannelTitles(localData.compBox, 180, holdMaxHeight);
			// defer
			{
				if (localData.channelTitleScrollView.notNil, {
					localData.channelTitleScrollView.visibleOrigin = dataBank.channelTitleVisibleOrigin;
				});
			}.defer(0.05);
		});

		if (localData.addEditSelection == "+Source", {
			arrSourceModules.do({arg item, i;
				var btnAdd, holdBlend, holdCol = TXColor.sysGuiCol1;
				var newModuleClass = item[1];
				var holdSymbol = item[0].keep(2-item[0].size).asSymbol;
				// if category
				if ((item[0].keep(2) == "> ") or: (item[0].keep(2) == "v "), {
					//  category toggle
					holdView = StaticText(localData.compBox, Rect(0,0, 170, 20));
					holdView.string = item[0];
					if (localData.closedSourceCats[holdSymbol].isNil, {
						holdBlend = 0.01;
					},{
						holdBlend = 0.5;
					});
					if (item[0].keep(7).keep(-5) == "Audio", {
						holdCol = TXColor.sysGuiCol1.blend(TXColor.grey(0.5), holdBlend);
					},{
						holdCol = TXColor.sysGuiCol2.blend(TXColor.grey(0.5), holdBlend);
					});
					holdView.stringColor_(TXColor.white).background_(holdCol);
					holdView.align_(\left);
					holdView.mouseDownAction = { // toggle closed status
						if (localData.closedSourceCats[holdSymbol].isNil, {
							localData.closedSourceCats[holdSymbol] = true;
						},{
							localData.closedSourceCats[holdSymbol] = nil;
						});
						system.showView;
					};
				},{
					// if  valid add button
					if (newModuleClass.notNil, {
						if (newModuleClass.moduleRate == "audio", {
							holdCol = TXColor.sysGuiCol1;
						},{
							holdCol = TXColor.sysGuiCol2;
						});
						btnAdd = Button(localData.compBox, 26 @ 20);
						btnAdd.states = [["add", TXColor.white, holdCol]];
						btnAdd.action = {
							if (newModuleClass.notNil, {
								TXChannelRouting.addModule(newModuleClass);
								TXChannelRouting.setScrollToEndChannel;
								system.showView;
							});
						};
					});
					//  text
					holdView = StaticText(localData.compBox, Rect(0,0, 140, 20));
					holdView.string = item[0];
					holdView.background_(TXColor.white).stringColor_(TXColor.black.blend(holdCol, 0.9));
					holdView.align_(\left);
				});
			});
			// defer
			{
				if (localData.allModulesScrollView.notNil, {
					localData.allModulesScrollView.visibleOrigin = dataBank.allModulesVisibleOrigin;
				});
			}.defer(0.05);

		});

		if (localData.addEditSelection == "+Insert", {
			arrInsertModules.do({arg item, i;
				var holdCol = TXColor.sysGuiCol1;
				var btnAdd, holdBlend, boxDrag, holdDragLabel, holdDragObject;
				var newModuleClass = item[1];
				var holdSymbol = item[0].keep(2-item[0].size).asSymbol;
				// if category
				if ((item[0].keep(2) == "> ") or: (item[0].keep(2) == "v "), {
					//  category toggle
					holdView = StaticText(localData.compBox, Rect(0,0, 170, 20));
					holdView.string = item[0];
					if (localData.closedInsertCats[holdSymbol].isNil, {
						holdBlend = 0.01;
					},{
						holdBlend = 0.5;
					});
					if (item[0].keep(7).keep(-5) == "Audio", {
						holdCol = TXColor.sysGuiCol1.blend(TXColor.grey(0.5), holdBlend);
					},{
						holdCol = TXColor.sysGuiCol2.blend(TXColor.grey(0.5), holdBlend);
					});
					holdView.stringColor_(TXColor.white).background_(holdCol);
					holdView.align_(\left);
					holdView.mouseDownAction = { // toggle closed status
						if (localData.closedInsertCats[holdSymbol].isNil, {
							localData.closedInsertCats[holdSymbol] = true;
						},{
							localData.closedInsertCats[holdSymbol] = nil;
						});
						system.showView;
					};
				},{
					// if  valid add Add button
					if (newModuleClass.notNil, {
						holdDragObject = ();
						holdDragObject.newModuleClass = newModuleClass;
						if (newModuleClass.moduleRate == "audio", {
							holdDragObject.type = "audioInsert";
							holdCol = TXColor.sysGuiCol1;
							holdDragLabel = "audio insert: " ++ item[1].defaultName;
						},{
							holdDragObject.type = "controlInsert";
							holdCol = TXColor.sysGuiCol2;
							holdDragLabel = "control insert: " ++ item[1].defaultName;
						});
						boxDrag = DragSource(localData.compBox, 28 @ 20);
						boxDrag.object = holdDragObject;
						boxDrag.stringColor_(TXColor.white).background_(holdCol);
						boxDrag.dragLabel = holdDragLabel;
						boxDrag.string = " drag";
						boxDrag.align = 'left';
					});
					//  text
					holdView = StaticText(localData.compBox, Rect(0,0, 140, 20));
					holdView.string = item[0];
					holdView.background_(TXColor.white).stringColor_(TXColor.black.blend(holdCol, 0.9));
					holdView.align_(\left);
				});

			});
			// defer
			{
				if (localData.channelTitleScrollView.notNil, {
					localData.channelTitleScrollView.visibleOrigin = dataBank.channelTitleVisibleOrigin;
				});
			}.defer(0.05);

		});

		if (localData.addEditSelection == "+Channel", {
			arrChannelSources.do({arg item, i;
				var holdCol = TXColor.sysGuiCol1;
				var btnAdd, holdBlend;
				var holdSourceBus = item[1];
				var holdSymbol = item[0].keep(2-item[0].size).asSymbol;
				// if category
				if ((item[0].keep(2) == "> ") or: (item[0].keep(2) == "v "), {
					//  category toggle
					holdView = StaticText(localData.compBox, Rect(0,0, 170, 20));
					holdView.string = item[0];
					if (localData.closedChannelCats[holdSymbol].isNil, {
						holdBlend = 0.01;
					},{
						holdBlend = 0.5;
					});
					if (item[0].keep(7).keep(-5) == "Audio", {
						holdCol = TXColor.sysGuiCol1.blend(TXColor.grey(0.5), holdBlend);
					},{
						holdCol = TXColor.sysGuiCol2.blend(TXColor.grey(0.5), holdBlend);
					});
					holdView.stringColor_(TXColor.white).background_(holdCol);
					holdView.align_(\left);
					holdView.mouseDownAction = { // toggle closed status
						if (localData.closedChannelCats[holdSymbol].isNil, {
							localData.closedChannelCats[holdSymbol] = true;
						},{
							localData.closedChannelCats[holdSymbol] = nil;
						});
						system.showView;
					};
				},{
					//  text
					// if  valid add +Ch button
					if (holdSourceBus.notNil, {
						if (holdSourceBus.class.moduleRate == "audio", {
							holdCol = TXColor.sysGuiCol1;
						},{
							holdCol = TXColor.sysGuiCol2;
						});
						btnAdd = Button(localData.compBox, 26 @ 20);
						btnAdd.states = [["add", TXColor.white, holdCol]];
						btnAdd.action = {
							if (holdSourceBus.notNil, {
								// add new channel from source module
								TXChannelRouting.addChannel(item[1]);
								TXChannelRouting.setScrollToEndChannel;
								system.showView;
							});
						};
					});
					holdView = StaticText(localData.compBox, Rect(0,0, 140, 20));
					holdView.string = item[0];
					holdView.background_(TXColor.white).stringColor_(TXColor.black.blend(holdCol, 0.9));
					holdView.align_(\left);
				});

			});
			// defer
			{
				if (localData.channelTitleScrollView.notNil, {
					localData.channelTitleScrollView.visibleOrigin = dataBank.channelTitleVisibleOrigin;
				});
			}.defer(0.05);
		});

		if (localData.addEditSelection == "+Meter", {
			// modules
			arrMeterSources.do({arg item, i;
				var holdCol = TXColor.sysGuiCol1;
				var btnAdd, holdBlend;
				var holdSymbol = item[0].keep(2-item[0].size).asSymbol;
				// if category
				if ((item[0].keep(2) == "> ") or: (item[0].keep(2) == "v "), {
					//  category toggle
					holdView = StaticText(localData.compBox, Rect(0,0, 170, 20));
					holdView.string = item[0];
					if (localData.closedMeterCats[holdSymbol].isNil, {
						holdBlend = 0.01;
					},{
						holdBlend = 0.5;
					});
					if (item[0].keep(7).keep(-5) == "Audio", {
						holdCol = TXColor.sysGuiCol1.blend(TXColor.grey(0.5), holdBlend);
					},{
						holdCol = TXColor.sysGuiCol2.blend(TXColor.grey(0.5), holdBlend);
					});
					holdView.stringColor_(TXColor.white).background_(holdCol);
					holdView.align_(\left);
					holdView.mouseDownAction = { // toggle closed status
						if (localData.closedMeterCats[holdSymbol].isNil, {
							localData.closedMeterCats[holdSymbol] = true;
						},{
							localData.closedMeterCats[holdSymbol] = nil;
						});
						system.showView;
					};
				},{
					if (item[2] == "audio", {
						holdCol = TXColor.sysGuiCol1;
					},{
						holdCol = TXColor.sysGuiCol2;
					});
					// if  valid add  button
					btnAdd = Button(localData.compBox, 26 @ 20);
					btnAdd.states = [["add", TXColor.white, TXColor.grey(0.5).blend(holdCol, 0.5)]];
					btnAdd.action = {
						item[1].value;
						system.showView;
					};
					//  text
					holdView = StaticText(localData.compBox, Rect(0,0, 140, 20));
					holdView.string = item[0];
					holdView.background_(TXColor.white).stringColor_(TXColor.black.blend(holdCol, 0.9));
					holdView.align_(\left);
				});
			});
			// defer
			{
				if (localData.allModulesScrollView.notNil, {
					localData.allModulesScrollView.visibleOrigin = dataBank.allModulesVisibleOrigin;
				});
			}.defer(0.05);
		});

		if (localData.addEditSelection == "Edit Module", {
			moduleCatString = nil;
			// modules
			arrEditModuleNames.do({arg item, i;
				var holdModule, btnModule, btnDelete, stringCol, backCol, btnModuleCol;
				var holdClass, holdClassSymbol, holdTextView;

				// if category
				if ((item[0].keep(2) == "> ") or: (item[0].keep(2) == "v "), {
					moduleCatString = item[0];
					holdLastCat = moduleCatString.keep(2 - moduleCatString.size);
				},{
					holdModule = arrEditModules.detect({ arg module;
						module.notNil and: {module.instDisplayName == item[0]};
					});
					if (holdModule.class.moduleRate == "audio", {
						btnModuleCol = TXColor.sysGuiCol1;
					},{
						btnModuleCol = TXColor.sysGuiCol2;
					});
					backCol = btnModuleCol;
					if (moduleCatString.notNil, {
						holdClass = moduleCatString.keep(2 - moduleCatString.size);
						holdClassSymbol = holdClass.asSymbol;
						//  category toggle
						holdView = Button(localData.compBox, Rect(0,0, 10, 20));
						holdView.states = [[moduleCatString.keep(1), TXColor.white, TXColor.sysGuiCol1]];
						holdView.action = { // toggle closed status
							if (localData.closedEditCats[holdClassSymbol].isNil, {
								localData.closedEditCats[holdClassSymbol] = true;
							},{
								localData.closedEditCats[holdClassSymbol] = nil;
							});
							system.showView;
						};
					},{
						holdTextView = StaticText(localData.compBox, Rect(0,0, 10, 20))
						.background = TXColor.sysMainWindow;
						if (this.moduleNameRemoveNo(item[0]) == holdLastCat, {
							holdTextView.align_('center').string_("L").stringColor_(TXColor.white);
						});
					});
					if (TXChannelRouting.displayModule == holdModule, {
						stringCol = TXColor.sysSelectedModString;
					},{
						stringCol = TXColor.white;
					});
					// button -  module
					btnModule = Button(localData.compBox, 140 @ 20);
					btnModule.states = [[holdModule.instDisplayName, stringCol, backCol]];
					btnModule.action = {
						TXChannelRouting.displayModule = holdModule;
						TXChannelRouting.showModuleBox = true;
						system.showWindow = 'Modules & Channels';
						system.showFrontScreen = false;
						system.addHistoryEvent; // add to history
						system.showView;
					};
					// button -  delete
					btnDelete = Button(localData.compBox, 22 @ 20);
					btnDelete.states = [["Del", TXColor.white, TXColor.sysDeleteCol]];
					btnDelete.action = {arg view;
						var windowPoint = view.mapToGlobal(22 @ 20);
						holdModule.confirmDeleteModule(windowPoint.x,
							Window.screenBounds.height - 150 - windowPoint.y);
						system.showView;
					};
					moduleCatString = nil;
				});
			});
			// defer
			{
				if (localData.allModulesScrollView.notNil, {
					localData.allModulesScrollView.visibleOrigin = dataBank.allModulesVisibleOrigin;
				});
			}.defer(0.05);
		});
	}

	*adjustClosedCategories{ arg inArray, dataBank, localData, closedCatDict, alwaysAddFirstItem = false;
		var holdCategory, outArray, startString, outString, catCount;
		outArray = [];
		holdCategory = nil;
		catCount = 999;
		inArray.do({arg item, i;
			// if item is category, adjust open/closed string
			if (item[0][0].asString == ">", {
				catCount = 0; // reset
				holdCategory = item[0].keep(1 - item[0].size).asSymbol;
				if (closedCatDict[holdCategory].isNil, {
					startString = "> ";
				}, {
					startString = "v ";
				});
				item[0] = startString ++ item[0].keep(1 - item[0].size);
				outArray = outArray.add(item);
			}, {
				catCount = catCount + 1;
				// add to array if category not closed,
				if (closedCatDict[holdCategory].isNil
					or: (alwaysAddFirstItem and: catCount == 1) // or if first item,
					or: (localData.addEditSelection == "Edit Module"   // or if edit modeul & not in a category
						and: localData.allCategories.indexOfEqual(this.moduleNameRemoveNo(item[0])).isNil)
					, {
						outArray = outArray.add(item);
				});
			});
		});
		^outArray;
	}

	*openAllCategories{arg localData;
		if (localData.addEditSelection == "+Source", {
			localData.closedSourceCats = ();
		});
		if (localData.addEditSelection == "+Channel", {
			localData.closedChannelCats = ();
		});
		if (localData.addEditSelection == "+Insert", {
			localData.closedInsertCats = ();
		});
		if (localData.addEditSelection == "+Meter", {
			localData.closedMeterCats = ();
		});
		if (localData.addEditSelection == "Edit Module", {
			localData.closedEditCats = ();
		});
	}

	*closeAllCategories{arg localData;
		if (localData.addEditSelection == "+Source", {
			localData.allCategories.do({ arg item, i;
				localData.closedSourceCats[item.asSymbol] = true;
			});
		});
		if (localData.addEditSelection == "+Channel", {
			localData.allCategories.do({ arg item, i;
				localData.closedChannelCats[item.asSymbol] = true;
			});
		});
		if (localData.addEditSelection == "+Insert", {
			localData.allCategories.do({ arg item, i;
				localData.closedInsertCats[item.asSymbol] = true;
			});
		});
		if (localData.addEditSelection == "+Meter", {
			localData.allCategories.do({ arg item, i;
				localData.closedMeterCats[item.asSymbol] = true;
			});
		});
		if (localData.addEditSelection == "Edit Module", {
			localData.allCategories.do({ arg item, i;
				localData.closedEditCats[item.asSymbol] = true;
			});
		});
	}

	*openFreqScope{arg dataBank, holdString, busArray;
		TXFreqScope.rebuildWindow(holdString, TXChannelRouting.system.server, busArray);
		// {
		// 	if (FreqScope.scopeOpen == true and: dataBank.holdFreqScope.notNil, {
		// 		dataBank.holdFreqScope.window.close;
		// 		0.1.wait;
		// 	});
		// 	dataBank.holdFreqScope = FreqScope.new(800, 400, busArray.asArray.at(0));
		// 	holdString = dataBank.holdFreqScope.window.name + holdString + "(bus no."
		// 	+ busArray.asArray.at(0) ++ ")";
		// 	dataBank.holdFreqScope.window.name = holdString;
		// }.fork(AppClock);
	}

	*openOscilloscope{arg holdString, busArray, scopeRate = 'audio';
			TXOscilloscope.rebuildWindow(holdString, TXChannelRouting.system.server, busArray, scopeRate);
	}

	*moduleNameRemoveNo{arg argString;
		var outString, index;
		index = argString.findBackwards("[");
		if (index.notNil, {
			outString = argString.keep(index - 1); // also remove space before "["
		},{
			outString = argString;
		});
		^outString;
	}
}


