// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSignalFlowView {
//	var resizeHandles, resizeFixed, dropX, dropY;
//	var isSelected=false;
	var window, <>userView, arrAllModules, arrChannels, arrFXBusses, system, owner;
	var selection, selectionChanged = false, <selectedViews;
	var <gridStep = 10,<gridOn = false, dragging = false, indent, multipleDragBy;
	var boxWidth = 130, boxHeight = 22;
	var height, width, holdString, prevPoint, movingScrollView, dragShiftAction;

	*new { arg argWindow, dimensions, argSystem, argOwner;
		^super.new.init(argWindow, dimensions, argSystem, argOwner);
	}
	init { arg argWindow, dimensions, argSystem, argOwner;
		window = argWindow;
		system = argSystem;
		owner = argOwner;
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
		//userView = UserView(window,Rect(0,0, width, height));
		userView = ScaledUserView(window,Rect(0,0, width, height)
			//, Rect(-800,-800, 3200, 3200) // testing XXX
			//, Rect(0, 0, width * 2, height * 2) // testing XXX
			//, Rect(0-width , 0-height, width * 2, height * 2) // testing XXX
		);
//		userView.scale = 2;
		userView.mouseDownAction = { |v,x,y,m| this.mouseDown(x,y,m) };
		userView.mouseUpAction = { |v,x,y,m| this.mouseUp(x,y,m) };
		userView.mouseMoveAction = { |v,x,y,m| this.drag(x,y,m) };
//		userView.background_(TXColor.sysModuleWindow);
		userView.background_(Color.grey(0.9, 0.2));
		userView.drawFunc = {
			// draw grid
		//	this.drawGrid;

			// Draw outline box
			Pen.width = 2;
			Pen.strokeColor = TXColor.white;
			Pen.addRect(Rect(1,1, width-2, height-2));
			Pen.stroke;

			// draw lines for each channel connection
			arrChannels.do({ arg argChannel, i;
				var arrModules, nonDestModules, channelModules;
				arrModules = [argChannel.sourceModule, argChannel, argChannel.insert1Module,
					argChannel.insert2Module, argChannel.insert3Module,
					argChannel.insert4Module, argChannel.insert5Module
				];
				nonDestModules = arrModules.reject({arg item; item.isNil});
				// draw channel inclusion lines
				channelModules = nonDestModules.keep(1 - nonDestModules.size);
				(channelModules.size - 1).do({arg i;
					this.drawChannelConnection(argChannel.channelRate, channelModules[i], channelModules[i+1]);
				});
				// draw bus connection lines
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
				var holdRect, holdDefaultCol, holdRate, holdModuleType;
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
					Pen.fillColor = Color.gray(0.7);
					//Pen.addRect(holdRect.insetBy(8));
					4.do({arg i;
						var gap = (i + 1) * boxHeight / 5;
						Pen.addRect(Rect(holdRect.left, holdRect.top + gap, boxWidth, 1.5));
					});
					Pen.fill;
				}, {
					if (argModule.class.moduleType == "bus", {
						Pen.fillColor = Color.gray(0.65).alpha_(0.75);
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
				if (	((holdRate ==  "audio") and:
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

	drawConnection {arg channelRate, fromModule, toModule;
		var offsetX;
		// set variables
		if (channelRate == "audio", {
			offsetX = 82;
			if (fromModule.highlight or: toModule.highlight, {
				Pen.width = 3;
				Pen.strokeColor = TXColor.paleBlue2;
			},{
				Pen.width =2 ;
				Pen.strokeColor = TXColor.sysGuiCol1;
			});
		},{
			offsetX = 52;
			if (fromModule.highlight or: toModule.highlight, {
				Pen.width = 3;
				Pen.strokeColor = TXColor.paleGreen;
			},{
				Pen.width = 2;
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

	drawChannelConnection {arg channelRate, fromModule, toModule;
		//Pen.strokeColor = TXColor.grey(0.7);
		if (channelRate == "audio", {
			Pen.strokeColor = TXColor.sysGuiCol1;
		},{
			Pen.strokeColor = TXColor.sysGuiCol2;
		});
		Pen.width = 0.75;
		Pen.moveTo((fromModule.posX) @ (fromModule.posY + 6 + boxHeight));
		Pen.lineTo((toModule.posX) @ (toModule.posY + 6));
		Pen.stroke;
		Pen.moveTo((fromModule.posX + boxWidth) @ (fromModule.posY + 6 + boxHeight));
		Pen.lineTo((toModule.posX + boxWidth) @ (toModule.posY + 6));
		Pen.stroke;
	}

	mouseDown { |x,y, mod|
		var view, point, handle;

		point = x @ y;

		if (mod.isCtrl == true, {
			movingScrollView = true;
			prevPoint = point;
		},{
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
		});
	}

	mouseUp { |x,y, mod|
		movingScrollView = false;
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

	dragShiftAction {arg shiftX, shiftY;
		var origin = owner.classData.layoutsScrollView.visibleOrigin;
		var newX = (origin.x - shiftX).max(0);
		var newY = (origin.y - shiftY).max(0);
		owner.classData.layoutsScrollView.visibleOrigin = Point(newX, newY);
	}

	drag { |x,y, mod|
		var view, f, point = x @ y, xMin, yMin, shiftX, shiftY;
		if (mod.isCtrl == true or: (movingScrollView == true), {
			shiftX = x - prevPoint.x;
			shiftY = y - prevPoint.y;
			this.dragShiftAction(shiftX * 0.85 * owner.classData.sigFlowScale, shiftY * 0.85 * owner.classData.sigFlowScale);
			prevPoint = point;
		},{
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
			owner.adjustSigFlowSizeBeyond(Point(view.posX, view.posY), {system.showView;});
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

