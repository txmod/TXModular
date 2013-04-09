// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSignalFlowView {
//	var resizeHandles, resizeFixed, dropX, dropY;
//	var isSelected=false;
	var window, <>userView, arrAllModules, arrChannels, arrFXBusses, system;
	var selection, selectionChanged = false, <selectedViews;
	var <gridStep = 10,<gridOn = false, dragging = false, indent, multipleDragBy;
	var boxWidth = 130, boxHeight = 22;
	var height, width, holdString;

	*new { arg argWindow, dimensions, argSystem;
		^super.new.init(argWindow, dimensions, argSystem);
	}
	init { arg argWindow, dimensions, argSystem;
		window = argWindow;
		system = argSystem;
		dimensions = dimensions.bounds;
		height = dimensions.height;
		width = dimensions.width;
		arrChannels = TXChannelRouting.arrChannels;
		arrFXBusses = system.arrFXSendBusses;
		arrAllModules = system.arrSystemModules
			++ system.arrAllBusses
			++ arrChannels;
		selectedViews = arrAllModules.select({arg item, i; item.highlight == true;});
		// make UserView
		userView = UserView(window,Rect(0,0, width, height));
		userView.mouseDownAction = { |v,x,y,m| this.mouseDown(x,y,m) };
		userView.mouseUpAction = { |v,x,y,m| this.mouseUp(x,y,m) };
		userView.mouseMoveAction = { |v,x,y,m| this.drag(x,y,m) };
//		userView.background_(TXColor.sysModuleWindow);
		userView.background_(Color.grey(0.9, 0.2));
		userView.drawFunc = {
			// draw grid
		//	this.drawGrid;
			// draw lines for each channel connection
			arrChannels.do({ arg argChannel, i;
				var arrModules, nonDestModules;
				arrModules = [argChannel.sourceModule, argChannel, argChannel.insert1Module,
					argChannel.insert2Module, argChannel.insert3Module,
					argChannel.insert4Module, argChannel.insert5Module
				];
				nonDestModules = arrModules.reject({arg item; item.isNil});
				if ( argChannel.chanStatus == "active", {
					arrModules = arrModules.add(argChannel.destModule);
				});
				arrModules = arrModules.reject({arg item; item.isNil});
				(arrModules.size - 1).do({arg i;
					this.drawConnection(argChannel.channelRate, arrModules[i], arrModules[i+1]);
				});
				if ( argChannel.chanStatus == "active", {
					if ( argChannel.getSynthArgSpec("FXSend1On") == 1, {
						this.drawConnection(argChannel.channelRate, nonDestModules.last, arrFXBusses[0]);
					});
					if ( argChannel.getSynthArgSpec("FXSend2On") == 1, {
						this.drawConnection(argChannel.channelRate, nonDestModules.last, arrFXBusses[1]);
					});
					if ( argChannel.getSynthArgSpec("FXSend3On") == 1, {
						this.drawConnection(argChannel.channelRate, nonDestModules.last, arrFXBusses[2]);
					});
					if ( argChannel.getSynthArgSpec("FXSend4On") == 1, {
						this.drawConnection(argChannel.channelRate, nonDestModules.last, arrFXBusses[3]);
					});
				});
			});
			// make boxes for each module
			arrAllModules.do({ arg argModule, i;
				var holdRect, holdSmallRect, holdDefaultCol, holdRate, holdModuleType;
				holdRect = Rect(argModule.posX, argModule.posY+6, boxWidth, boxHeight);
				if (argModule.class.moduleType == "channel", {
					if (argModule.channelRate == "audio", {
						holdDefaultCol = TXColor.sysGuiCol1;
						holdRate = "audio";
					},{
						holdDefaultCol = TXColor.sysGuiCol2;
						holdRate = "control";
					});
				},{
					if (argModule.class.moduleRate == "audio", {
						holdDefaultCol = TXColor.sysGuiCol1;
						holdRate = "audio";
					},{
						holdDefaultCol = TXColor.sysGuiCol2;
						holdRate = "control";
					});
				});
				// Draw the fill
				Pen.fillColor = holdDefaultCol;
				Pen.addRect(holdRect);
				Pen.fill;
				if (argModule.class.moduleType == "channel", {
					Pen.fillColor = Color.new255(0, 0, 60);
					Pen.addRect(holdRect.insetBy(8));
					Pen.fill;
				}, {
					if (argModule.class.moduleType == "bus", {
						Pen.fillColor = Color.new255(0, 0, 60);
						Pen.addRect(holdRect.insetBy(5));
						Pen.fill;
					});
				});
				Pen.width =2;
				if (argModule.highlight, {
					Pen.strokeColor = TXColor.white;
				},{
					Pen.strokeColor = holdDefaultCol;
				});
				// Draw the frame
				Pen.addRect(holdRect);
				Pen.stroke;
				// Draw the connectors
				holdModuleType = argModule.class.moduleType;
				Pen.strokeColor = TXColor.sysGuiCol2;
				if (((holdRate ==  "control") and:
						((holdModuleType == "insert") or: (holdModuleType == "bus")
							or: (holdModuleType == "channel")
						)
					) or:
					(argModule.class.arrCtlSCInBusSpecs.asArray.size > 0), {
					Pen.addRect(Rect(argModule.posX+50, argModule.posY, 4, 4););
				});
				if ((holdRate ==  "control") and:
					 	((argModule.class.noOutChannels > 0) or: (holdModuleType == "channel")), {
					Pen.addRect(Rect(argModule.posX+50, argModule.posY+30, 4, 4););
				});
				Pen.stroke;
				Pen.strokeColor = TXColor.sysGuiCol1;
				if (		((holdRate ==  "audio") and:
							((holdModuleType == "insert") or: (holdModuleType == "bus")
								or: (holdModuleType == "channel")
							)
						) or:
						(argModule.class.arrAudSCInBusSpecs.asArray.size > 0),
				{
					Pen.addRect(Rect(argModule.posX+80, argModule.posY, 4, 4););
				});
				if ((holdRate ==  "audio") and:
					 	((argModule.class.noOutChannels > 0) or: (holdModuleType == "channel")), {
					Pen.addRect(Rect(argModule.posX+80, argModule.posY+30, 4, 4););
				});
				Pen.stroke;
				// add text
				Pen.color = TXColor.white;
				Pen.font = Font( "Helvetica", 12 );
				if (argModule.class.moduleType == "channel", {
					holdString = "Ch " ++ argModule.channelNo.asString ++ ": "
						++ argModule.getSourceBusName;
				},{
					holdString = argModule.instName;
				});
				Pen.stringCenteredIn(holdString, holdRect);
			});
			if( dragging == false and: selection.notNil, {
				// Draw the selection box
				Pen.width =1;
				Pen.strokeColor = TXColor.white;
				Pen.addRect(selection.rect);
				Pen.stroke;
			});

		};
	}
	drawConnection { arg channelRate, fromModule, toModule;
		var offsetX;
		// set variables
		if (channelRate == "audio", {
			offsetX = 82;
			if (fromModule.highlight or: toModule.highlight, {
				Pen.width =3;
				Pen.strokeColor = TXColor.paleBlue;
			},{
				Pen.width =2;
				Pen.strokeColor = TXColor.sysGuiCol1;
			});
		},{
			offsetX = 52;
			if (fromModule.highlight or: toModule.highlight, {
				Pen.width =3;
				Pen.strokeColor = TXColor.paleGreen;
			},{
				Pen.width =2;
				Pen.strokeColor = TXColor.sysGuiCol2;
			});
		});
		// draw connection
		Pen.moveTo((fromModule.posX+offsetX) @ (fromModule.posY+32));
		Pen.lineTo((fromModule.posX+offsetX) @ (fromModule.posY+39));
		Pen.lineTo((toModule.posX+offsetX) @ (toModule.posY-5));
		Pen.lineTo((toModule.posX+offsetX) @ (toModule.posY+2));
		Pen.stroke;
	}


	mouseDown { |x,y, mod|
		var view, point, handle;

		point = x @ y;

		view = this.viewContainingPoint(point);

		if (view.notNil, {
			this.highlightView(view);
		});

		dragging = view.notNil;

		if( dragging, {

			indent = point - Point(view.posX, view.posY);

			if (mod.isShift == true, {
				if ( not(selectedViews.includes(view)), {
					selectedViews = selectedViews.add(view);
				});
			});

			if( (selectedViews.size > 1) and:
				{ selectedViews.includes(view)},
			{
				multipleDragBy = view
			},{
				multipleDragBy = nil;
				selectedViews = [ view ]
			});
		},{
			if (mod.isShift != true, {
				this.unhighlightAllViews;
				selectedViews = [];
			});
			selection = TXAreaSelection(point);
		});
		userView.refresh;
	}

	mouseUp { |x,y, mod|
		if (selectionChanged == true, {
			if (mod.isShift == false, {
				selectedViews = [];
			});
			selectedViews = selectedViews ++ arrAllModules.select({ |view|
				selection.selects(Rect(view.posX, view.posY, boxWidth, boxHeight))
			});
			selectionChanged = false;
		});
		this.unhighlightAllViews;
		this.highlightSelectedViews;
		this.fitToGrid;
		this.checkLayoutBoundaries;
		if(selection.notNil,{
			selection = nil;
		});
		userView.refresh;
	}

	drag { |x,y|
		var view, f, point = x @ y, xMin, yMin;
		if( dragging, {
			if(multipleDragBy.notNil,
			{
				f = point - ( Point(multipleDragBy.posX, multipleDragBy.posY) + indent );
				/* get the minimum posX and posY from selectedViews
				use these to set mininum values for f.x and f.y */
				xMin = selectedViews.collect({ |v| v.posX;}).minItem;
				yMin = selectedViews.collect({ |v| v.posY;}).minItem;
				f.x = f.x.max(xMin.neg);
				f.y = f.y.max(yMin.neg);

				selectedViews.do({ |v|
					v.posX = (v.posX + f.x).max(0);
					v.posY = (v.posY + f.y).max(0);
				})
			},{
				view = selectedViews.first;
				f = point - indent;
				view.posX = f.x.max(0);
				view.posY = f.y.max(0);
			})
		},
		{
			selection.mouseDrag(point);
			selectionChanged = true;
			userView.refresh;
		});
		userView.refresh;
	}

	viewContainingPoint { |point|
		arrAllModules.do({ |view|
			var viewRect;
			viewRect = Rect(view.posX, view.posY+6, boxWidth, boxHeight);
			if (viewRect.containsPoint(point),
				{ ^view });
		});
		^nil
	}
	highlightView{ arg view;
		if (view.highlight == false, {
			this.unhighlightAllViews;
			view.highlight = true;
		});
	}

	highlightSelectedViews {
		selectedViews.do({ |view|
			view.highlight = true;
		})
	}
	unhighlightAllViews {
		arrAllModules.do({ |view|
			view.highlight = false;
		})
	}
	checkLayoutBoundaries {
		// only highlighted modules could have moved.
		// if they have make sure they're not too near view boundaries.
		selectedViews.do({ |view|
			TXSignalFlow.sizeAdjustBeyond(Point(view.posX, view.posY), {system.showView;});
		})
	}
	fitToGrid {
		selectedViews.do({ |view|
			view.posX = view.posX.round(gridStep);
			view.posY = view.posY.round(gridStep);
		})
	}
	drawGrid {
		// draw grid lines if gridStep > 1
		if (gridStep > 1, {

			Pen.use {
				Pen.strokeColor = Color.black.alpha_(0.1);
				Pen.width = 1;
				height.do({ |y|
					if (y % gridStep == 0, {
						// draw line
						Pen.moveTo(0 @ y+6);
						Pen.lineTo(width @ y+6);
					});
				});
				width.do({ |x|
					if (x % gridStep == 0, {
						// draw line
						Pen.moveTo(x @ 0);
						Pen.lineTo(x @ height);
					});
				});
				Pen.stroke;
			};
		});
	}
}

