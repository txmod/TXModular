// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCurveDraw {	// MultiSlider, popup and buttons for curve drawing with 5 user slots for saving curves
	classvar <>classData;
	var <>localData, <labelView, <dragView, <userView, <popupView, <popupView2, <txSoundFile;
	var <>action, <value, <arrSlotData;
	var <drawModeBtn, <histNextBtn, <histPrevBtn, <curveList, <lineQuantPopup;
	//var <multiSliderView, <lineDrawView;
	var linearArray, resetArray, tableSize, gridRows, gridCols, holdDrawCurve, isDrawing, isVertical;
	var holdFunction1, holdFunction2, holdSoundFile, holdMouseData, holdLastDrawPoint;

	*initClass{
		classData = ();
		classData.drawMode = 'point';   // 'point' or 'line'
		classData.lineWarpInd = 0;
		classData.lineQuant = 0;   // 0/1/2/3 ["quant off", "quant x+y", "quant x", "quant y"]
	}

	*new { arg window, dimensions, label, action, initVal, initAction=false, labelWidth=80, initSlotVals,
		showPresets, curveWidth=270, curveHeight=257, resetAction="Ramp",
		gridRowsFunc, gridColsFunc, xLabel, yLabel, curveThumbSize=1,
		showBuilderButton = true, dataEvent, drawVertical=false;

		^super.new.init(window, dimensions, label, action, initVal, initAction, labelWidth, initSlotVals,
			showPresets, curveWidth, curveHeight, resetAction, gridRowsFunc, gridColsFunc,
			xLabel, yLabel, curveThumbSize, showBuilderButton, dataEvent, drawVertical);
	}

	init { arg window, dimensions, label, argAction, initVal, initAction, labelWidth, initSlotVals,
		showPresets, curveWidth, curveHeight, resetAction,gridRowsFunc, gridColsFunc,
		xLabel, yLabel, curveThumbSize, showBuilderButton = true, dataEvent, drawVertical;

		var arrGenFunctions, arrModifyFunctions, popItems, popAction, newArray, holdLastX, holdLastY;
		var holdTop, holdLeft, columnView, userViewWidth, userViewHeight, curveListHeight, curveBackground;
		var dragStartPoint, dragEndPoint, dragStartPointNorm, dragEndPointNorm, dragLabelWidth;
		var holdSFData, holdMouseData;

		// init
		holdMouseData = ();
		holdMouseData.scaledXPos = 0.0;
		holdMouseData.scaledYPos = 0.0;
		holdMouseData.xPos = 0.0;
		holdMouseData.yPos = 0.0;
		holdMouseData.mouseIsDown = false;
		// use local data if Event passed
		if (dataEvent.notNil and: {dataEvent.class == Event}, {
			localData = dataEvent;
			// add default data if empty
			localData.drawMode = localData.drawMode ? 'point';
			localData.lineWarpInd = localData.lineWarpInd ? 0;
			localData.lineQuant = localData.lineQuant ? 0;
			localData.maxHistorySize = localData.maxHistorySize ? 20;
			localData.curveHistory = localData.curveHistory ? [];
			localData.curveHistIndex = localData.curveHistIndex ? 0;
		});

		initVal = initVal ? Array.newClear(128).seriesFill(0, 1/127);
		tableSize = initVal.size;
		linearArray = Array.newClear(tableSize).seriesFill(0, 1 / (tableSize - 1));
		resetArray = linearArray;
		if (resetAction == "Ramp", {
			resetArray = linearArray;
		});
		if (resetAction == "Zero", {
			resetArray = Array.newClear(tableSize).fill(0);
		});
		if (resetAction == "Max", {
			resetArray = Array.newClear(tableSize).fill(1);
		});
		if (resetAction == "Flat50", {
			resetArray = Array.newClear(tableSize).fill(0.5);
		});
		if (resetAction == "Sine", {
			resetArray = Signal.sineFill(tableSize, [1.0],[1.5pi])
			.collect({arg item, i; (item.value + 1) * 0.5;});
		});
		initSlotVals = initSlotVals ? resetArray.dup(5);
		arrSlotData = initSlotVals;
		action = argAction;
		xLabel = (xLabel ? "Input").as(String);
		yLabel = (yLabel ? "Output").as(String);
		gridRows = gridRowsFunc.value ?? 2;
		gridCols = gridColsFunc.value ?? 2;
		userViewWidth = curveWidth - 4;
		userViewHeight = curveHeight - 4;
		isDrawing = false;
		curveBackground = Color.grey(0.7);
		isVertical = drawVertical ? false;

		// columnView
		columnView = CompositeView(window, labelWidth @ userViewHeight);
		columnView.decorator = FlowLayout(columnView.bounds);
		columnView.decorator.margin = Point(0,0);
		columnView.decorator.gap.x = 2;
		columnView.decorator.gap.y = 2;
		columnView.decorator.reset;

		// label
		labelView = StaticText(columnView, labelWidth @ 20);
		labelView.string = label;
		labelView.stringColor = TXColor.sysGuiCol1;
		labelView.background = TXColor.white;
		labelView.align = \right;

		if (localData.notNil, {
			// <> buttons
			histPrevBtn = Button(columnView, (labelWidth * 0.5) -1 @ 16)
			.states_([["<", TXColor.paleYellow, TXColor.sysGuiCol1]])
			.action_({
				this.loadCurveHistoryPrev;
			});
			histNextBtn = Button(columnView, (labelWidth * 0.5) -1 @ 16)
			.states_([[">", TXColor.paleYellow, TXColor.sysGuiCol1]])
			.action_({
				this.loadCurveHistoryNext;
			});
			columnView.decorator.nextLine.shift(0,4);
			this.coloriseHistoryButtons;
		});

		// Drag box
		if (localData.notNil, {
			dragLabelWidth = labelWidth - 28;
		},{
			dragLabelWidth = labelWidth;
		});
		dragView = DragBoth(columnView, labelWidth @ 26);
		dragView.setBoth = false;
		dragView.string = "drag & drop";
		dragView.align = \center;
		dragView.background_(TXColor.paleYellow);
		dragView.stringColor_(TXColor.sysGuiCol1);
		dragView.canReceiveDragHandler = {View.currentDrag.isKindOf(Array);};
		dragView.receiveDragHandler = {arg view;
			var newArray;
			newArray = this.resampleArray(View.currentDrag.value, tableSize);
			this.valueAction_(newArray);
			//view.focus(false);
		};
		dragView.object = initVal;
		dragView.beginDragAction = {arg view;
			dragView.dragLabel = "Curve (size: " ++ view.object.size.asString ++")" ;
			view.object;
		};
		columnView.decorator.shift(0,6);

		if (showBuilderButton == true, {
			// create button
			Button(columnView, labelWidth @ 20)
			.states_([["Curve Builder", TXColor.paleYellow, TXColor.sysGuiCol1]])
			.action_({
				TXCurveBuilder.openWithCurve(this.value);
			});
			columnView.decorator.shift(0,6);
		});
		// draw mode
		drawModeBtn = Button(columnView, labelWidth @ 20)
		.states_([
			["< Draw Points", TXColor.white, TXColor.sysGuiCol1],
			["< Draw Lines", TXColor.white, TXColor.sysGuiCol1]])
		.action_({arg view;
			if (view.value == 0, {
				this.setDrawMode('point');
			},{
				this.setDrawMode('line');
			});
		});
		if (this.getDrawMode == 'line', {
			drawModeBtn.value = 1;
		},{
			drawModeBtn.value = 0;
		});

		// Line Quant
		// lineQuantPopup = TXCheckBox(columnView, labelWidth @ 20, "Line Quant",
		// TXColor.sysGuiCol1, TXColour.white, TXColor.white, TXColor.sysGuiCol1)
		// .value_(this.getLineQuant)
		// .action_({arg view;
		// 	this.setLineQuant(view.value);
		// });
		lineQuantPopup = PopUpMenu(columnView, labelWidth @ 20);
		lineQuantPopup.items_(["quant off", "quant x+y", "quant x", "quant y"]);
		lineQuantPopup.value_(this.getLineQuant);
		lineQuantPopup.action_({arg view;
			this.setLineQuant(view.value);
		});

		curveListHeight = userViewHeight - columnView.decorator.top - 22;
		// Curve  list
		curveList =  TXListView(columnView, labelWidth @ curveListHeight)
		.items_(TXLineWarp.getLineWarpStrings)
		.value_(this.getLineWarpInd)
		.action_({arg view;
			this.setLineWarpInd(view.value);
		});

		// soundfile
		if (localData.notNil and: {localData.showSoundFile == 1}, {
			holdLeft = window.asView.decorator.left;
			holdTop = window.asView.decorator.top;
			/* OLD
			txSoundFile = TXSoundFile(window, (userViewWidth - 14)@ (userViewHeight - 14), {},
			localData.soundFileStartPos, localData.soundFileEndPos,
			false, localData.soundFileName, showZoomControls: 0,
			onLoadAction: {
			txSoundFile.soundFileView.zoomSelection(0);
			txSoundFile.soundFileView.yZoom(1.1);
			});
			*/
			// store data & use
			if (localData.soundFileData.isNil, {
				holdSoundFile = SoundFile.new;
				holdSoundFile.openRead(localData.soundFileName);
				localData.soundFileNumChannels = holdSoundFile.numChannels;
				localData.soundFileSampleRate = holdSoundFile.sampleRate;
				holdSFData = FloatArray.newClear(holdSoundFile.numFrames * holdSoundFile.numChannels);
				holdSoundFile.readData(holdSFData);
				localData.soundFileData = holdSFData;
			});
			txSoundFile = TXSoundFile(window, (userViewWidth - 14)@ (userViewHeight - 14), {},
				localData.soundFileStartPos, localData.soundFileEndPos,
				false, nil, showZoomControls: 0,
			);
			if (localData.soundFileData.asArray.size > 0, {
				txSoundFile.soundFileView.setData(localData.soundFileData.asArray, 1024, 0,
					localData.soundFileNumChannels, localData.soundFileSampleRate);
				txSoundFile.lo = localData.soundFileStartPos;
				txSoundFile.hi = localData.soundFileEndPos;
				txSoundFile.soundFileView.zoomSelection(0);
				txSoundFile.soundFileView.yZoom(1.1);
			});
			window.asView.decorator.left = holdLeft;
			window.asView.decorator.top = holdTop;
			curveBackground = Color.grey(0.3).alpha_(0.3);
		});

		/*   ------------- OLD CODE -------
		// create grid
		userView = UserView(window, userViewWidth @ userViewHeight);
		userView.drawFunc = {arg view;
		Pen.color = TXColor.white;
		gridRows.do({arg item, i;
		var holdHeight;
		holdHeight = ((userViewHeight * (i + 1)) / gridRows).asInteger;
		Pen.line(0 @ holdHeight, userViewWidth @ holdHeight);
		});
		gridCols.do({arg item, i;
		var holdWidth;
		holdWidth = ((userViewWidth * (i + 1)) / gridCols).asInteger;
		Pen.line(holdWidth @ 0, holdWidth @ userViewHeight);
		});
		Pen.stringAtPoint(yLabel, 18 @ (userViewHeight/2)-10 );
		Pen.stringAtPoint(xLabel, userViewWidth/2 @ userViewHeight-18 );
		Pen.stroke;
		};
		// // decorator shift
		// if (window.class == Window, {
		// 	window.view.decorator.shift(0-userViewWidth-6, 0);
		// 	}, {
		// 		window.decorator.shift(0-userViewWidth-6, 0);
		// });

		// create curve on userView
		multiSliderView = MultiSliderView(userView, Rect(-2, -2, curveWidth, curveHeight));
		//multiSliderView.gap_(0);
		if (tableSize > 128, {multiSliderView.gap_(0)});
		multiSliderView.elasticMode(1);
		multiSliderView.valueThumbSize_(curveThumbSize);
		multiSliderView.indexThumbSize_(curveThumbSize);
		multiSliderView.drawLines_(true);
		multiSliderView.drawRects_(true);
		multiSliderView.background_(Color.new(1,1,1,0));
		// multiSliderView.action = {
		multiSliderView.mouseUpAction = {arg view;
		this.valueAction_(view.value);
		};
		// multiSliderView.canReceiveDragHandler = {View.currentDrag.isKindOf(Array);};
		multiSliderView.canReceiveDragHandler = false;
		// multiSliderView.receiveDragHandler = {arg view;
		// 	var newArray;
		// 	newArray = this.resampleArray(View.currentDrag.value, tableSize);
		// 	view.valueAction_(newArray);
		// 	view.focus(false);
		// };


		// lineDrawView
		//lineDrawView = UserView(multiSliderView, Rect(2, 2, userViewWidth, userViewHeight));
		lineDrawView = UserView(userView.view, Rect(0, 0, userViewWidth, userViewHeight));
		lineDrawView.background_(Color.clear);
		lineDrawView.drawFunc = {arg view;
		var pt1, pt2;
		if (dragEndPoint.notNil, {
		Pen.color = TXColor.red;
		Pen.line(dragStartPoint, dragEndPoint);
		Pen.circle(Rect.aboutPoint(dragStartPoint, 2, 2));
		Pen.circle(Rect.aboutPoint(dragEndPoint, 2, 2));
		Pen.stroke;
		if (this.getLineQuant == 1, {
		Pen.color = TXColor.orange;
		pt1 = Point(dragStartPointNorm.x.round(gridCols.reciprocal) * view.bounds.width,
		dragStartPointNorm.y.round(gridRows.reciprocal) * userViewHeight);
		pt2 = Point(dragEndPointNorm.x.round(gridCols.reciprocal) * view.bounds.width,
		dragEndPointNorm.y.round(gridRows.reciprocal) * userViewHeight);
		Pen.line(pt1, pt2);
		Pen.circle(Rect.aboutPoint(pt1, 2, 2));
		Pen.circle(Rect.aboutPoint(pt2, 2, 2));
		Pen.stroke;
		});
		});
		};
		lineDrawView.mouseDownAction = {arg view, x, y;
		x = x.min(userViewWidth);
		y = y.min(userViewHeight);
		dragStartPoint = Point(x, y);
		dragStartPointNorm = Point(x / userViewWidth, y / userViewHeight);
		};
		lineDrawView.mouseMoveAction = {arg view, x, y;
		dragEndPoint = Point(x, y);
		dragEndPointNorm = Point(x / userViewWidth, y / userViewHeight);
		lineDrawView.refresh;
		};
		lineDrawView.mouseUpAction = {arg view, x, y;
		if (dragEndPoint.notNil, {
		this.addLineToCurve(dragStartPointNorm, dragEndPointNorm);
		});
		dragStartPoint = nil;
		dragEndPoint = nil;
		lineDrawView.refresh;
		};

		--------------------- END OF OLD CODE ----------------
		*/

		// create ScaledUserView
		userView = ScaledUserViewContainer(window, userViewWidth @ userViewHeight,
			Rect(-0.001,-0.001,1.001,1.001));
		userView.view.background_(curveBackground);
		userView.drawFunc = {arg view;
			var fixWidth = view.pixelScale.x;
			var fixWidth2 = fixWidth * 2;
			var fixHeight = view.pixelScale.y;
			var fixHeight2 = fixHeight * 2;
			var lastIndex = value.size - 1;
			var pt1, pt2, holdArray;
			// create grid
			Pen.color = TXColor.white;
			gridRows.do({arg item, i;
				var holdHeight;
				holdHeight = ((i + 1)) / gridRows;
				Pen.addRect(Rect(0, holdHeight, 1, fixHeight));
			});
			gridCols.do({arg item, i;
				var holdWidth;
				holdWidth = ((i + 1)) / gridCols;
				Pen.addRect(Rect(holdWidth, 0, fixWidth, 1));
			});
			Pen.fill;
		};
		userView.unscaledDrawFunc = {arg view;
			var lastIndex = value.size - 1;
			var holdQuant, pt1, pt2, startX, startY, endX, endY, holdArray, holdOrientation;
			var xOffset, yOffset, pointString;
			// axis labels
			if (isVertical, {
				holdOrientation = "    direction: vertical";
			}, {
				holdOrientation = "    direction: horizontal";
			});
			Pen.color = Color(0.6, 0.4, 0);
			//Pen.color = TXColor.orange2;
			Pen.stringAtPoint("size: " ++ value.size.asString ++ holdOrientation, 10 @ 10);
			Pen.stringAtPoint(yLabel, 18 @ (userViewHeight/2)-10 );
			Pen.stringAtPoint(xLabel, userViewWidth/2 @ userViewHeight-30 );
			Pen.stroke;

			// draw mouse point
			if (holdMouseData.mouseIsDown, {
				// xOffset = 10 - (100 * (holdMouseData.xPos / userViewWidth));
				// yOffset = -20 + (40 * (holdMouseData.yPos / userViewHeight));
				if (holdMouseData.xPos > (userViewWidth * 0.5), {
					xOffset = -90;
				}, {
					xOffset = 10;
				});
				if (holdMouseData.yPos < (userViewHeight * 0.5), {
					yOffset = -20;
				}, {
					yOffset = 20;
				});
				Pen.color = TXColor.black;
				pointString = "( " ++ holdMouseData.scaledXPos.round(0.001).asString ++ ", "
				++ holdMouseData.scaledYPos.round(0.001).asString ++ " )";
				Pen.stringAtPoint(pointString,
					(holdMouseData.xPos + xOffset) @ (userViewHeight - holdMouseData.yPos + yOffset) );
			});

			// draw curve
			if (isDrawing, {
				holdArray = holdDrawCurve;
			},{
				holdArray = value;
			});
			if ((view.scaleH > 2.8) or: {(view.scaleV > 2.8)}, {
				Pen.width = 1.5;
			},{
				Pen.width = 2;
			});
			Pen.color = TXColor.white;
			holdArray.do({arg item, i;
				var holdPoint1, holdPoint2, holdColor;
				// draw translucent line for shading
				if (isVertical, {
					holdPoint1 = view.translateScale( Point(item, (i / lastIndex)) );
					holdPoint2 = view.translateScale( Point(0, (i + 1) / lastIndex) );
				}, {
					holdPoint1 = view.translateScale( Point(i / lastIndex, 1 - item) );
					holdPoint2 = view.translateScale( Point((i + 1) / lastIndex, 127) );
				});
				holdColor = TXColor.sysGuiCol1.blend(Color.black, 0.5);
				Pen.color = holdColor.alpha_(0.15);
				Pen.line(holdPoint1, holdPoint2);
				Pen.stroke;
				// draw line between every pair
				if (i < lastIndex, {
					if (isVertical, {
						holdPoint2 = view.translateScale( Point( holdArray[i + 1], ((i + 1) / lastIndex)) );
					}, {
						holdPoint2 = view.translateScale( Point((i + 1) / lastIndex, 1 - holdArray[i + 1]) );
					});
					Pen.color = TXColor.black.blend(TXColor.red, 0.35);
					Pen.line(holdPoint1, holdPoint2);
					Pen.stroke;
				});
				// draw circles if scale  > 2.8
				if ((view.scaleH > 2.8) or: {(view.scaleV > 2.8)}, {
					Pen.color = holdColor;
					Pen.circle(Rect.aboutPoint(holdPoint1, 2, 2));
					Pen.stroke;
				});
			});
			// if line draw
			if (this.getDrawMode == 'line', {
				if (dragEndPoint.notNil, {
					Pen.color = TXColor.red;
					pt1 = view.translateScale(dragStartPoint);
					pt2 = view.translateScale(dragEndPoint);
					Pen.line(pt1, pt2);
					Pen.circle(Rect.aboutPoint(pt1, 2, 2));
					Pen.circle(Rect.aboutPoint(pt2, 2, 2));
					Pen.stroke;
					holdQuant = this.getLineQuant;
					if (holdQuant > 0, {
						case
						{holdQuant == 1} { //quant x + y
							startX = dragStartPoint.x.round(gridCols.reciprocal);
							startY = dragStartPoint.y.round(gridRows.reciprocal);
							endX = dragEndPoint.x.round(gridCols.reciprocal);
							endY = dragEndPoint.y.round(gridRows.reciprocal);
						}
						{holdQuant == 2} { //quant x
							startX = dragStartPoint.x.round(gridCols.reciprocal);
							startY = dragStartPoint.y;
							endX = dragEndPoint.x.round(gridCols.reciprocal);
							endY = dragEndPoint.y;
						}
						{holdQuant == 3} { //quant y
							startX = dragStartPoint.x;
							startY = dragStartPoint.y.round(gridRows.reciprocal);
							endX = dragEndPoint.x;
							endY = dragEndPoint.y.round(gridRows.reciprocal);
						};
						pt1 = view.translateScale(Point(startX, startY));
						pt2 = view.translateScale(Point(endX, endY));
						Pen.color = TXColor.orange;
						Pen.line(pt1, pt2);
						Pen.circle(Rect.aboutPoint(pt1, 2, 2));
						Pen.circle(Rect.aboutPoint(pt2, 2, 2));
						Pen.stroke;
					});
				});
			});
		};
		userView.mouseDownAction = { |v, sx, sy, m, x, y, inside|
			var sXYPoint;
			sx = sx.max(0).min(1);
			sy = sy.max(0).min(1);
			sXYPoint = Point(sx, sy);
			holdMouseData.scaledXPos = sx;
			holdMouseData.scaledYPos = 1 - sy;
			holdMouseData.xPos = x.max(0).min(userViewWidth);
			holdMouseData.yPos = userViewHeight - y.max(0).min(userViewHeight);
			holdMouseData.mouseIsDown = true;
			if (this.getDrawMode == 'line', {
				dragStartPoint = sXYPoint;
				if (m.isShift and: {holdLastDrawPoint.notNil}) {
					this.addLineToCurve(holdLastDrawPoint, sXYPoint);
				};
			},{
				isDrawing = true;
				holdDrawCurve = value.copy;
				holdLastX = sx;
				holdLastY = sy;
				this.applyMouseDrag(sx, sy, sx, sy);
			});
			holdLastDrawPoint = sXYPoint;
			userView.refresh;
		};
		userView.mouseMoveAction = { |v, sx, sy, m, x, y, inside|
			var sXYPoint;
			sx = sx.max(0).min(1);
			sy = sy.max(0).min(1);
			sXYPoint = Point(sx, sy);
			holdMouseData.scaledXPos = sx;
			holdMouseData.scaledYPos = 1 - sy;
			holdMouseData.xPos = x.max(0).min(userViewWidth);
			holdMouseData.yPos = userViewHeight - y.max(0).min(userViewHeight);
			if (this.getDrawMode == 'line', {
				dragEndPoint = sXYPoint;
			},{
				this.applyMouseDrag(holdLastX, holdLastY, sx, sy);
				holdLastX = sx;
				holdLastY = sy;
			});
			userView.refresh;
		};
		userView.mouseUpAction = { |v, sx, sy, m, x, y, inside|
			var sXYPoint;
			sx = sx.max(0).min(1);
			sy = sy.max(0).min(1);
			sXYPoint = Point(sx, sy);
			holdMouseData.mouseIsDown = false;
			if (this.getDrawMode == 'line', {
				if (dragEndPoint.notNil, {
					this.addLineToCurve(dragStartPoint, dragEndPoint);
				});
				dragStartPoint = nil;
				dragEndPoint = nil;
				userView.refresh;
			},{
				isDrawing = false;
				//run action
				this.finishMouseDrag;
			});
			holdLastDrawPoint = sXYPoint;
		};
		// when zoom changes, update sound file zoom
		if (txSoundFile.notNil, {
			holdFunction1 = userView.scaleSliders[0].action;
			userView.scaleSliders[0].action = {arg view;
				// run original action first
				holdFunction1.value(userView.scaleSliders[0]);
				this.updateSoundFileZoom;
			};
			holdFunction2 = userView.moveSliders[0].action;
			userView.moveSliders[0].action = {arg view;
				// run original action first
				holdFunction2.value(userView.moveSliders[0]);
				this.updateSoundFileZoom;
			};
		});

		// decorator store settings
		if (window.class == Window, {
			holdTop = window.view.decorator.top;
			holdLeft = window.view.decorator.left;
		}, {
			holdTop = window.decorator.top;
			holdLeft = window.decorator.left;
		});

		// create button
		Button(window, 50 @ 20)
		.states_([["Reverse", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.value.reverse;
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Invert", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = 1 - this.value;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,24);
		}, {
			window.decorator.shift(-118,24);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Mirror", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.mirror(this.value);
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Mirror+Inv", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.mirrorInvert(this.value);
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,24);
		}, {
			window.decorator.shift(-118,24);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Smooth", TXColor.white, TXColor.sysGuiCol1]])
		.action_({ var inArr, outArr;
			inArr = this.value;
			inArr.size.do({arg item, i;
				if ((i > 0) and: (i < (inArr.size-1)), {
					//			outArr = outArr.add((inArr.at(i) + inArr.at(i-1)) / 2);
					outArr = outArr.add((inArr.at(i) + inArr.at(i-1)+ inArr.at(i+1)) / 3);
				},{
					outArr = outArr.add(inArr.at(i));
				});
			});
			this.valueAction = outArr;
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Smooth 5", TXColor.white, TXColor.sysGuiCol1]])
		.action_({ var inArr, outArr;
			inArr = this.value;
			5.do({arg stage, stageNo;
				outArr = [];
				inArr.size.do({arg item, i;
					if ((i > 0) and: (i < (inArr.size-1)), {
						//			outArr = outArr.add((inArr.at(i) + inArr.at(i-1)) / 2);
						outArr = outArr.add((inArr.at(i) + inArr.at(i-1)+ inArr.at(i+1)) / 3);
					},{
						outArr = outArr.add(inArr.at(i));
					});
				});
				inArr = outArr;
			});
			this.valueAction = outArr;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,24);
		}, {
			window.decorator.shift(-118,24);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Reset", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.reset;
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Zoom out", TXColor.white, TXColor.grey]])
		.action_({
			userView.scale_([1, 1]);
			userView.move_([0.5, 0.5]);
			this.updateSoundFileZoom;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,28);
		}, {
			window.decorator.shift(-118,28);
		});
		// create slot load and save buttons
		5.do({ arg i;
			Button(window, 50 @ 20)
			.states_([["Load  " ++ (i+1).asString, TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				this.loadSlot(i);
			});
			Button(window, 50 @ 20)
			.states_([["Store " ++ (i+1).asString, TXColor.white, TXColor.sysGuiCol2]])
			.action_({
				this.storeSlot(i);
			});
			// decorator shift
			if (window.class == Window, {
				window.view.decorator.shift(-118,24);
			}, {
				window.decorator.shift(-108,24);
			});
		});
		// decorator reset
		if (window.class == Window, {
			window.view.decorator.top = holdTop;
			window.view.decorator.left = holdLeft;
		}, {
			window.decorator.top = holdTop;
			window.decorator.left = holdLeft;
		});

		// decorator next line
		if (window.class == Window, {
			window.view.decorator.nextLine;
		}, {
			window.decorator.nextLine;
		});

		arrGenFunctions = this.getGenFunctions(showPresets);
		if (arrGenFunctions.notNil, {
			popItems = arrGenFunctions.collect({arg item, i; item.at(0);});
			popAction = {arg view;
				if (view.value > 0, {
					newArray = arrGenFunctions.at(view.value).at(1).value;
					this.valueAction = newArray;
				});
			};
			popupView = TXPopup(window, labelWidth + 180 @ 20, "", popItems, popAction,
				0, false, labelWidth);
			popupView.popupMenuView.stringColor_(TXColour.black).background_(TXColor.white);
		});

		// more actions:
		arrModifyFunctions = 	[
			["Modify curve ...", {}],
			[ "normalize", {this.valueAction = this.value.normalize;}],
			[ "double copy", {this.valueAction = this.doubleCopy(this.value);}],
			[ "triple copy", {this.valueAction = this.tripleCopy(this.value);}],
			[ "phase shift 25% = 1/4 = 90 deg", {this.valueAction = this.shiftPhase(this.value, 0.25);}],
			[ "phase shift 33.33% = 1/3 = 120 deg", {this.valueAction = this.shiftPhase(this.value, 0.33333);}],
			[ "phase shift 50% = 1/2 = 180 deg", {this.valueAction = this.shiftPhase(this.value, 0.5);}],
			[ "phase shift 66.66% = 2/3 = 240 deg", {this.valueAction = this.shiftPhase(this.value, 0.66666);}],
			[ "phase shift 75% = 3/4 = 270 deg", {this.valueAction = this.shiftPhase(this.value, 0.75);}],
		];
		popItems = arrModifyFunctions.collect({arg item, i; item.at(0);});
		popAction = {arg view;
			arrModifyFunctions.at(view.value).at(1).value;
			popupView2.popupMenuView.value = 0; // reset popup
			//{popupView2.popupMenuView.value = 0;}.defer(0.3); // reset popup
		};
		popupView2 = TXPopup(window, 210 @ 20, "", popItems, popAction,
			0, false, 4);
		popupView2.popupMenuView.stringColor_(TXColour.black).background_(TXColor.white);

		this.setDrawMode;

		if (initAction, {
			this.valueAction_(initVal);
		}, {
			this.value_(initVal);
		});
	}  // end of init

	value_ { arg argValue;
		if (value != argValue, {
			this.curveChanged(argValue);
			this.addToHistory(argValue);
			//multiSliderView.value = argValue;
			userView.refresh;
		});
	}
	valueAction_ { arg argValue;
		this.value_(argValue);
		action.value(this);
	}
	reset {
		this.valueAction = resetArray;
		if (popupView.notNil, {popupView.value = 0});
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? resetArray;
		if (initAction) {
			this.valueAction_(initVal);
		}{
			this.value(initVal);
		};
	}
	curveChanged {arg newArray;
		value = newArray;
		// set drag object
		if (dragView.notNil, {
			dragView.object = value;
		});
	}
	addToHistory {arg newArray;
		// if data is different add to history
		if (localData.notNil and: {localData.curveHistory[localData.curveHistIndex] != newArray}, {
			// truncate hist at current index
			localData.curveHistory = localData.curveHistory.keep(localData.curveHistIndex + 1);
			localData.curveHistory = localData.curveHistory.add(newArray);
			localData.curveHistory = localData.curveHistory.keep(0 - localData.maxHistorySize);
			localData.curveHistIndex = localData.curveHistory.size - 1;
			this.coloriseHistoryButtons;
		});
	}
	loadCurveHistoryPrev {
		var holdVal;
		if (localData.curveHistIndex > 0, {
			localData.curveHistIndex = localData.curveHistIndex - 1;
			holdVal = this.resampleArray(localData.curveHistory[localData.curveHistIndex], value.size);
			this.curveChanged(holdVal);
			//multiSliderView.value = holdVal;
			userView.refresh;
			this.coloriseHistoryButtons;
			action.value(this);
		});
	}
	loadCurveHistoryNext {
		var holdVal;
		if (localData.curveHistIndex < (localData.curveHistory.size - 1), {
			localData.curveHistIndex = localData.curveHistIndex + 1;
			holdVal = this.resampleArray(localData.curveHistory[localData.curveHistIndex], value.size);
			this.curveChanged(holdVal);
			//multiSliderView.value = holdVal;
			userView.refresh;
			this.coloriseHistoryButtons;
			action.value(this);
		});
	}
	coloriseHistoryButtons {
		if (histPrevBtn.notNil, {
			if (localData.curveHistIndex > 0, {
				histPrevBtn.states_([["<", TXColor.paleYellow, TXColor.sysGuiCol1]])
			},{
				histPrevBtn.states_([["<", TXColor.grey(0.7), TXColor.sysGuiCol1]])
			});
			if (localData.curveHistIndex < (localData.curveHistory.size - 1), {
				histNextBtn.states_([[">", TXColor.paleYellow, TXColor.sysGuiCol1]])
			},{
				histNextBtn.states_([[">", TXColor.grey(0.7), TXColor.sysGuiCol1]])
			});
		});
	}
	storeSlot { arg num;
		arrSlotData.put(num, this.value);
	}
	loadSlot { arg num;
		this.valueAction = this.resampleArray(arrSlotData.at(num), tableSize);
	}
	mirror { arg arrCurveValues;
		var outArray, startVal, midArray, endVal, holdSignal, newArray;
		outArray = arrCurveValues.deepCopy;
		startVal = outArray.first.asArray;
		midArray = outArray.drop(2).drop(-2);
		if ((midArray.size % 2) == 1, {
			midArray = midArray.add(outArray.last);
		});
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = outArray.last.asArray;
		outArray = startVal ++ midArray ++ endVal;
		outArray = outArray ++ outArray.copy.reverse;
		holdSignal = Signal.newFrom(outArray);
		newArray = Array.newFrom(holdSignal);
		if (newArray.size >  arrCurveValues.size, {
			newArray.removeAt((newArray.size/2).asInteger);
		});
		^newArray;
	}
	mirrorInvert { arg arrCurveValues;
		var outArray, startVal, midArray, endVal, holdSignal, newArray;
		outArray = arrCurveValues.deepCopy;
		startVal = outArray.first.asArray;
		midArray = outArray.drop(2).drop(-2);
		if ((midArray.size % 2) == 1, {
			midArray = midArray.add(outArray.last);
		});
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = outArray.last.asArray;
		outArray = startVal ++ midArray ++ endVal;
		//outArray = (outArray ++ outArray.copy.reverse.neg).normalize(0,1);
		outArray = ((outArray - 1) ++ (outArray.copy.reverse.neg + 1)).normalize(0,1);
		holdSignal = Signal.newFrom(outArray);
		newArray = Array.newFrom(holdSignal);
		if (newArray.size >  arrCurveValues.size, {
			newArray.removeAt((newArray.size/2).asInteger);
		});
		^newArray;
	}

	doubleCopy { arg arrCurveValues;
		// var copyArray, outArray, startVal, midArray, endVal, holdSignal;
		// copyArray = arrCurveValues.deepCopy;
		// startVal = copyArray.first.asArray;
		// midArray = copyArray.drop(2).drop(-2);
		// midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		// endVal = copyArray.last.asArray;
		// outArray = startVal ++ midArray ++ endVal;
		// outArray = outArray ++ outArray;
		// holdSignal = Signal.newFrom(outArray);
		// ^Array.newFrom(holdSignal);
		var outArray;
		outArray = arrCurveValues.size.collect({arg i; arrCurveValues.wrapAt(i * 2); });
		^outArray;
	}
	tripleCopy { arg arrCurveValues;
		// var copyArray, outArray, startVal, midArray, endVal, holdSignal;
		// copyArray = arrCurveValues.deepCopy;
		// startVal = copyArray.first.asArray;
		// midArray = copyArray.drop(3).drop(-3);
		// midArray = midArray.clump(3).collect({arg item, i; item.sum/item.size;});
		// endVal = copyArray.last.asArray;
		// outArray = startVal ++ midArray ++ endVal;
		// outArray = outArray ++ outArray ++ outArray;
		// holdSignal = Signal.newFrom(outArray);
		// ^Array.newFrom(holdSignal);
		var outArray;
		outArray = arrCurveValues.size.collect({arg i; arrCurveValues.wrapAt(i * 3); });
		^outArray;
	}
	shiftPhase { arg arrCurveValues, shiftPhaseAmount;
		var outArray, startIndex, midArray, holdSignal;
		if ((shiftPhaseAmount > 0) && (shiftPhaseAmount < 1), {
			midArray = arrCurveValues.deepCopy;
			startIndex = (shiftPhaseAmount * (arrCurveValues.size-1)).asInteger;
			outArray = midArray[startIndex..] ++ midArray[..(startIndex-1)];
			holdSignal = Signal.newFrom(outArray);
			^Array.newFrom(holdSignal);
			// ^outArray;
		},{
			^arrCurveValues;
		});
	}

	setGridRowsCols { arg rows, cols;
		gridRows = rows;
		gridCols = cols;
		userView.refresh;
	}

	getLineWarpInd{
		if (localData.notNil, {
			^localData.lineWarpInd;
		},{
			^classData.lineWarpInd;
		});
	}

	setLineWarpInd{arg argLineWarpInd;
		if (localData.notNil, {
			localData.lineWarpInd = argLineWarpInd;
		},{
			classData.lineWarpInd = argLineWarpInd;
		});
	}

	getLineQuant{
		if (localData.notNil, {
			^localData.lineQuant;
		},{
			^classData.lineQuant;
		});
	}

	setLineQuant{arg argLineQuant;
		if (localData.notNil, {
			localData.lineQuant = argLineQuant;
		},{
			classData.lineQuant = argLineQuant;
		});
	}

	getDrawMode{
		if (localData.notNil, {
			^localData.drawMode;
		},{
			^classData.drawMode;
		});
	}

	setDrawMode{arg argMode;
		var holdMode;
		if (localData.notNil, {
			if (argMode.notNil, {
				localData.drawMode = argMode;
			});
			holdMode = localData.drawMode;
		},{
			if (argMode.notNil, {
				classData.drawMode = argMode;
			});
			holdMode = classData.drawMode;
		});
		// show or hide views
		if (holdMode == 'point', {
			//lineDrawView.visible_(false);
			curveList.visible_(false);
			lineQuantPopup.visible_(false);
		});
		if (holdMode == 'line', {
			//lineDrawView.visible_(true);
			curveList.visible_(true);
			lineQuantPopup.visible_(true);
		});
	}

	addLineToCurve{arg point1, point2;
		var holdQuant, startInd, endInd, noSteps, mult, p1X, p1Y, p2X, p2Y;
		var outCurve = value.copy;
		var grainSize = outCurve.size.reciprocal;
		holdQuant = this.getLineQuant;
		if (holdQuant > 0, {
			case
			{holdQuant == 1} { //quant x + y
				p1X = point1.x.round(gridCols.reciprocal);
				p1Y = point1.y.round(gridRows.reciprocal);
				p2X = point2.x.round(gridCols.reciprocal);
				p2Y = point2.y.round(gridRows.reciprocal);
			}
			{holdQuant == 2} { //quant x
				p1X = point1.x.round(gridCols.reciprocal);
				p1Y = point1.y;
				p2X = point2.x.round(gridCols.reciprocal);
				p2Y = point2.y;
			}
			{holdQuant == 3} { //quant y
				p1X = point1.x;
				p1Y = point1.y.round(gridRows.reciprocal);
				p2X = point2.x;
				p2Y = point2.y.round(gridRows.reciprocal);
			};
			point1 = Point(p1X, p1Y);
			point2 = Point(p2X, p2Y);
		});
		if (isVertical, {
			startInd = (point1.y / grainSize).asInteger.max(0).min(outCurve.size - 1);
			endInd = (point2.y / grainSize).asInteger.max(0).min(outCurve.size - 1);
			if (startInd < endInd, {
				mult = 1;
			},{
				mult = -1;
			});
			noSteps = abs(endInd - startInd)+1;
			noSteps.do({arg item, i;
				var prop, holdVal;
				if (noSteps == 1, {
					holdVal = 1 - point2.x;
				},{
					prop = TXLineWarp.getLineWarpVal(i / (noSteps - 1), this.getLineWarpInd);
					holdVal = (1 - point1.x) + (prop * ((1 - point2.x) - (1 - point1.x)));
				});
				outCurve[startInd + (i * mult)] = 1 - holdVal; // invert y
			});
		},{
			startInd = (point1.x / grainSize).asInteger.max(0).min(outCurve.size - 1);
			endInd = (point2.x / grainSize).asInteger.max(0).min(outCurve.size - 1);
			if (startInd < endInd, {
				mult = 1;
			},{
				mult = -1;
			});
			noSteps = abs(endInd - startInd)+1;
			noSteps.do({arg item, i;
				var prop, holdVal;
				if (noSteps == 1, {
					holdVal = point2.y;
				},{
					prop = TXLineWarp.getLineWarpVal(i / (noSteps - 1), this.getLineWarpInd);
					holdVal = point1.y + (prop * (point2.y - point1.y));
				});
				outCurve[startInd + (i * mult)] = 1 - holdVal; // invert y
			});
		});
		this.valueAction = outCurve;
	}

	resampleArray {arg inArray, newSize;
		var oldSize = inArray.size;
		var outArray;
		if (inArray.size == newSize, {
			outArray = inArray;
		}, {
			outArray = newSize.asInteger.collect({ arg i;
				inArray.blendAt(((oldSize-1) * i / (newSize-1)));
			});
		});
		^outArray;
	}

	applyMouseDrag{arg point1X, point1Y, point2X, point2Y;
		var maxInd, startInd, endInd, numPoints, inc;
		maxInd = value.size - 1;
		if (isVertical, {
			startInd = point1Y * maxInd;
			endInd = point2Y * maxInd;
			numPoints = abs(endInd - startInd) + 1;
			if (endInd == startInd, {
				holdDrawCurve[endInd] = point2X;
			}, {
				if (endInd > startInd, {
					inc = 1;
				},{
					inc = -1;
				});
				numPoints.do({ arg i;
					holdDrawCurve[startInd + (i * inc)] = (point1X + ((i / numPoints) * (point2X - point1X)));
				});
			});
		}, {
			startInd = point1X * maxInd;
			endInd = point2X * maxInd;
			numPoints = abs(endInd - startInd) + 1;
			if (endInd == startInd, {
				holdDrawCurve[endInd] = 1 - point2Y;
			}, {
				if (endInd > startInd, {
					inc = 1;
				},{
					inc = -1;
				});
				numPoints.do({ arg i;
					holdDrawCurve[startInd + (i * inc)] = 1 - (point1Y + ((i / numPoints) * (point2Y - point1Y)));
				});
			});
		});
	}

	finishMouseDrag{
		this.valueAction = holdDrawCurve.copy;
	}

	updateSoundFileZoom {
		var scale, minVal, maxVal, maxLength, scaleRecip, offsetMax, offset;
		if (txSoundFile.notNil, {
			scale = userView.userView.scaleH;
			maxLength = localData.soundFileEndPos - localData.soundFileStartPos;
			scaleRecip = scale.reciprocal;
			offsetMax = maxLength * (1 - scaleRecip);
			offset = userView.userView.moveH * offsetMax;
			minVal = localData.soundFileStartPos + offset;
			maxVal = minVal + (maxLength * scaleRecip);
			txSoundFile.lo = minVal;
			txSoundFile.hi = maxVal;
			txSoundFile.soundFileView.zoomSelection(0);
		});
	}

	getGenFunctions {arg showPresets;
		var arrGenFunctions;
		switch (showPresets.asSymbol,
			//
			\Warp, {
				arrGenFunctions = [
					["Preset Curves ...", {this.value;}],
					["Linear (Ramp)", linearArray],
					["Sine", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, \sin).map(item); });}],
					["Exponential", {linearArray.deepCopy.collect({arg item, i;
						ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
					["-Exponential", {linearArray.deepCopy.collect({arg item, i;
						1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
					["Cosine", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, \cos).map(item); });}],
					["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
					["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
					["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
					["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
					["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
					["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
					["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
					["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
					["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
					["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
					["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
					["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
					["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
					["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
					["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
					["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
					["Sine Cycle Phase 0 deg.",
						{Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Sine Cycle Phase 90 deg.",
						{Signal.sineFill(tableSize, [1.0],[0.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Sine Cycle Phase 180 deg.",
						{Signal.sineFill(tableSize, [1.0],[pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Sine Cycle Phase 270 deg.",
						{Signal.sineFill(tableSize, [1.0],[1.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Double Ramp", {var ramp, rampSize, outArr;
						rampSize = (tableSize/2).asInteger;
						ramp = Array.newClear(rampSize).seriesFill(0, 1/(rampSize-1));
						outArr = (ramp ++ ramp ++ [0, 0, 0, 0]).keep(tableSize);
					}],
					["Triple Ramp", {var ramp, rampSize, outArr;
						rampSize = (tableSize/3).asInteger;
						ramp = Array.newClear(rampSize).seriesFill(0, 1/(rampSize-1));
						outArr = (ramp ++ ramp ++ ramp ++ [0, 0, 0, 0]).keep(tableSize);
					}],
					["Quadruple Ramp", {var ramp, rampSize, outArr;
						rampSize = (tableSize/4).asInteger;
						ramp = Array.newClear(rampSize).seriesFill(0, 1/(rampSize-1));
						outArr = (ramp ++ ramp ++ ramp ++ ramp ++ [0, 0, 0, 0]).keep(tableSize);
					}],
				];
			},
			//
			\Curve, {
				arrGenFunctions = [
					["Preset Curves ...", {this.value;}],
					["All Maximum",{1 ! tableSize;}],
					["All 0.5",{0.5 ! tableSize;}],
					["All Minimum",{0 ! tableSize;}],
					["Ramp", linearArray],
					["Sine", {Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Sine+1, last point = first", {
						var arr = Signal.sineFill(tableSize-1, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});
						arr = arr.asArray;
						arr = arr.add(arr[0]);
					}],
					["Cosine", {Signal.sineFill(tableSize, [1.0],[0.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Cosine+1, last point = first", {
						var arr = Signal.sineFill(tableSize-1, [1.0],[0.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});
						arr = arr.asArray;
						arr = arr.add(arr[0]);
					}],
					["Exponential", {linearArray.deepCopy.collect({arg item, i;
						ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
					["-Exponential", {linearArray.deepCopy.collect({arg item, i;
						1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
					["Triangle", { var holdArr;
						holdArr = Array.newClear(tableSize/2).seriesFill(0, 2 / (tableSize - 1));
						holdArr ++ holdArr.copy.reverse;}],
					["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
					["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
					["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
					["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
					["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
					["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
					["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
					["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
					["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
					["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
					["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
					["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
					["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
					["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
					["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
					["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
				];
			},
			//
			\XFade, {
				arrGenFunctions = [
					["Preset Curves ...", {this.value;}],
					["Linear", linearArray],
					["DJ Smooth", Env([0, 1], [1], -2).arrayFill(128)],
					["DJ Full", Env([0, 1, 1], [0.5, 0.5], \lin).arrayFill(128)],
					["DJ Cut", Env([0, 0, 1, 1], [0.5, 0, 0.5], \lin).arrayFill(128)],
					["DJ Scratch 99%", Env([0, 1, 1], [1, 127], \lin).arrayFill(128)],
					["DJ Scratch 95%", Env([0, 1, 1], [5, 95], \lin).arrayFill(128)],
					["DJ Scratch 90%", Env([0, 1, 1], [1, 9], \lin).arrayFill(128)],
					["DJ Scratch 80%", Env([0, 1, 1], [2, 8], \lin).arrayFill(128)],
					["DJ Scratch 70%", Env([0, 1, 1], [3, 7], \lin).arrayFill(128)],
					["Exponential", {linearArray.deepCopy.collect({arg item, i;
						ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
					["-Exponential", {linearArray.deepCopy.collect({arg item, i;
						1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
					["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
					["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
					["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
					["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
					["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
					["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
					["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
					["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
					["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
					["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
					["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
					["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
					["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
					["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
					["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
					["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
				];
			},
			//
			\LFO, {
				arrGenFunctions = [
					["Preset Curves ...", {this.value;}],
					["Sawtooth", linearArray],
					["Sine", {Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Cosine", {Signal.sineFill(tableSize, [1.0],[0.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
					["Exponential", {linearArray.deepCopy.collect({arg item, i;
						ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
					["-Exponential", {linearArray.deepCopy.collect({arg item, i;
						1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
					["Triangle", { var holdArr;
						holdArr = Array.newClear(tableSize/2).seriesFill(0, 2 / (tableSize - 1));
						holdArr ++ holdArr.copy.reverse;}],
					["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
					["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
					["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
					["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
					["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
					["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
					["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
					["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
					["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
					["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
					["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
					["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
					["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
					["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
					["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
					["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
				];
			},
			//
			\Velocity, {
				arrGenFunctions = [
					["Preset Curves ...", {this.value;}],
					["Linear (Ramp)", {linearArray.deepCopy;}],
					["All Maximum, like an organ",{1 ! tableSize;}],
					["All 0.5",{0.5 ! tableSize;}],
					["Heavy 8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
					["Heavy 7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
					["Heavy 6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
					["Heavy 5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
					["Heavy 4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
					["Heavy 3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
					["Heavy 2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
					["Heavy 1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
					["Light 1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
					["Light 2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
					["Light 3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
					["Light 4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
					["Light 5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
					["Light 6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
					["Light 7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
					["Light 8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
					["Custom 1",{TXCustomVelCurves.arrCurves[0];}],
					["Custom 2",{TXCustomVelCurves.arrCurves[1];}],
					["Custom 3",{TXCustomVelCurves.arrCurves[2];}],
					["Custom 4",{TXCustomVelCurves.arrCurves[3];}],
					["Custom 5",{TXCustomVelCurves.arrCurves[4];}],
					["Custom 6",{TXCustomVelCurves.arrCurves[5];}],
					["Custom 7",{TXCustomVelCurves.arrCurves[6];}],
					["Custom 8",{TXCustomVelCurves.arrCurves[7];}],
				];
			},
			//
			\Waveshaper, {
				arrGenFunctions = [
					["Preset Curves ...", {this.value;}],
					["Linear (Ramp)", linearArray],
					["Sine-shaped Compress", {linearArray.deepCopy.collect({arg item, i;
						ControlSpec(0, 1, \cos).map(item); });}],
					["Compress 8", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });)}],
					["Compress 7", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });)}],
					["Compress 6", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });)}],
					["Compress 5", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });)}],
					["Compress 4", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });)}],
					["Compress 3", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });)}],
					["Compress 2", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });)}],
					["Compress 1", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });)}],
					["Expand 1", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });)}],
					["Expand 2", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });)}],
					["Expand 3", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });)}],
					["Expand 4", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });)}],
					["Expand 5", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });)}],
					["Expand 6", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });)}],
					["Expand 7", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });)}],
					["Expand 8", {this.mirrorInvert(
						linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });)}],
				];
			}
		);  // end of switch
		^arrGenFunctions;
	}

}
