// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXInterfaceLayoutView {
	var resizeHandles, resizeFixed, dropX, dropY;
	var window, <>userView, arrWidgets;
	var selection, selectionChanged = false, <selectedViews;
	var <>highlightActionFunc, <>mouseUpActionFunc;
	var <>gridStep = 1,<gridOn = true, dragging = false, indent, multipleDragBy;
	var layoutWidth, layoutHeight, startDragPoint;

	*new { arg argWindow, dimensions, argArrWidgets;
		^super.new.init(argWindow, dimensions, argArrWidgets);
	}
	init { arg argWindow, dimensions, argArrWidgets;
		arrWidgets = argArrWidgets;
		selectedViews = arrWidgets.select({arg item, i; item.highlight == true;});
		window = argWindow;
		dimensions = dimensions.bounds;
		layoutHeight = dimensions.height;
		layoutWidth = dimensions.width;
		indent = 0 @ 0;
		// make UserView
		userView = UserView(window, dimensions);
		userView.acceptsMouseOver = true;
		userView.mouseDownAction = { |v,x,y,m| this.mouseDown(x,y,m) };
		userView.mouseUpAction = { |v,x,y,m| this.mouseUp(x,y,m) };
		userView.mouseMoveAction = { |v,x,y,m| this.drag(x,y,m) };
		userView.background_(Color.clear);
		userView.drawFunc = {
			// draw grid
			this.drawGrid;
			// make boxes for each module
			arrWidgets.do({ arg argWidget, i;
				var holdRect, holdSmallRect, holdRate, holdModuleType;
				holdRect = Rect(argWidget.fromLeft(layoutWidth),
					argWidget.fromTop(layoutHeight), argWidget.width, argWidget.height);
				// Draw the fill
				Pen.fillColor = TXColor.white.alpha_(0.3);
				Pen.addRect(holdRect);
				Pen.fill;
				Pen.width =2;
				if (argWidget.highlight, {
					if( resizeHandles.notNil and: {resizeFixed.notNil}, {
						Pen.strokeColor = TXColor.orange;
					},{
						Pen.strokeColor = TXColor.red;
					});
				},{
					Pen.strokeColor = TXColor.black;
				});
				// Draw the frame
				Pen.addRect(holdRect);
				Pen.stroke;
				// add text
				Pen.color = TXColor.red;
				Pen.font = Font( "Helvetica", 12);
				//Pen.stringCenteredIn("W " ++ argWidget.widgetID.asString, holdRect);
				Pen.stringRightJustIn("W " ++ argWidget.widgetID.asString ++ " ", holdRect);
			});
			resizeHandles.do({ |r|
				Pen.color = TXColor.white;
				Pen.fillRect(r)
			});
			if( dragging == false and: selection.notNil, {
				// Draw the selection box
				Pen.width =1;
				Pen.strokeColor = TXColor.white;
				Pen.addRect(selection.rect);
				Pen.stroke;
			});

		};
		userView.mouseOverAction = { |v,x,y, mod|
			dropX = x; dropY = y;
		};
		userView.beginDragAction_({ arg view, x, y;
 			var holdArrViews, holdView;
			dropX = x; dropY = y;
 			startDragPoint = Point(dropX, dropY);
 			holdView = this.viewContainingPoint(startDragPoint);
 			// if holdView is nil don't do anything
			if (holdView.notNil, {
  				// (if click is in a selected view then leave as is)
 				// if a non-selected view then set selected views to this view
				if (not(selectedViews.includes(holdView)), {
					selectedViews = [holdView];
				});

			});
			if (selectedViews.size == 0, {
				userView.dragLabel_("No Widget selected.");
			});
			if (selectedViews.size == 1, {
				userView.dragLabel_("Clone single Widget - drag to new position");
			});
			if (selectedViews.size > 1, {
				userView.dragLabel_("Clone multiple Widgets - drag to new position");
			});
  			// make copy of array of selected views & give new IDs
			holdArrViews = selectedViews.deepCopy;
			holdArrViews.do({arg item, i;
				//	create widgetID
				item.newWidgetID;
			});
			// return holdArrViews
			holdArrViews;
 		});
		userView.canReceiveDragHandler = {
			View.currentDrag.asArray[0].isKindOf( TXWidget )
		};
		userView.receiveDragHandler = { |v,x,y, mod|
			var arrNewWidgets, diffPoint;
			dropX = x; dropY = y;
			if (startDragPoint == nil, {
				startDragPoint = Point(0,layoutHeight);
			});
			if (startDragPoint != nil and: {startDragPoint != Point( nil, nil) }, {
				diffPoint = Point(dropX,dropY) - startDragPoint;
				// minimum move 10 pixels
				if (abs(diffPoint.x) < 10, {
					if (diffPoint.x.isNegative, {
						diffPoint.x = -10;
					},{
						diffPoint.x = 10;
					});
				});
				if (abs(diffPoint.y) < 10, {
					if (diffPoint.y.isNegative, {
						diffPoint.y = -10;
					},{
						diffPoint.y = 10;
					});
				});
				arrNewWidgets = View.currentDrag.asArray;
				arrNewWidgets.do({ arg item, i;
					item.bounds_(
						item.bounds(layoutWidth, layoutHeight).moveBy(diffPoint.x, diffPoint.y),
						layoutWidth, layoutHeight
					);
				});
				// update all arrWidgets
				TXFrontScreen.arrWidgets = TXFrontScreen.arrWidgets ++ arrNewWidgets;
				arrWidgets = TXFrontScreen.arrWidgets;
				selectedViews = arrNewWidgets;
				dragging = true;
				// testing xxx - check next line?? - based on older code
				indent = dropX@dropY - Point(arrWidgets.last.fromLeft(layoutWidth), arrWidgets.last.fromTop(layoutHeight));
				this.updateResizeHandles;
				selectionChanged = false;
				this.unhighlightAllViews;
				this.mouseUp(dropX,dropY);
			});
		};
 		this.updateResizeHandles;
		userView.refresh;
	}

	mouseDown { |x,y, mod|
		var view, point, handle;
		//dropX = x; dropY = y;
		point = x @ y;

		view = this.viewContainingPoint(point);

		if (view.notNil, {
			this.highlightView(view);
		});

		dragging = view.notNil;

		if( resizeHandles.notNil and: {
			(handle = resizeHandles.detect({ |h| h.containsPoint(point) }) ).notNil
		},
		{
			this.setResizeFixed(handle)
		},
		{
			if( dragging, {

				indent = point - Point(view.fromLeft(layoutWidth), view.fromTop(layoutHeight));

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
 		//dropX = x; dropY = y;
		if (selectionChanged == true, {
			if (mod.isShift == false, {
				selectedViews = [];
			});
			selectedViews = selectedViews ++ arrWidgets.select({ |view|
				selection.selects(Rect(view.fromLeft(layoutWidth),
					view.fromTop(layoutHeight), view.width, view.height))
			});
			selectionChanged = false;
		});
		resizeFixed = nil;
		this.highlightSelectedViews;
		//this.fitToGridSelectedViews;
		if(selection.notNil,{
			selection = nil;
		});
		this.updateResizeHandles;
		if (mouseUpActionFunc.notNil, {
			mouseUpActionFunc.value;
		});
		userView.refresh;
	}

	drag { |x,y|
		var view, f = 0 @ 0, point = x @ y, xMin, yMin, xMax, yMax;
		if( dragging, {
			if( resizeFixed.isNil,
			{
				if(multipleDragBy.notNil,
				{
					f = point - ( Point(multipleDragBy.fromLeft(layoutWidth),
						multipleDragBy.fromTop(layoutHeight))
						 + indent );
					/* get the minimum/max fromLeft and fromTop from selectedViews
					use these to set mininum/max values for f.x and f.y */
					xMin = selectedViews.collect({ |v| v.fromLeft(layoutWidth);}).minItem;
					yMin = selectedViews.collect({ |v| v.fromTop(layoutHeight);}).minItem;
					xMax = selectedViews.collect({ |v| v.fromLeft(layoutWidth) + v.width;}).maxItem;
					yMax = selectedViews.collect({ |v| v.fromTop(layoutHeight) + v.height;}).maxItem;
					f.x = f.x.max(xMin.neg).min(layoutWidth - xMax);
					f.y = f.y.max(yMin.neg).min(layoutHeight - yMax);

					selectedViews.do({ |v|
						v.fromLeft_(((v.fromLeft(layoutWidth) + f.x).max(0)
							.min(layoutWidth - v.width)), layoutWidth) ;
						v.fromTop_(((v.fromTop(layoutHeight) + f.y).max(0)
							.min(layoutHeight - v.height)), layoutHeight);
					})
				},{
					view = selectedViews.first;
					f = point - indent;
					view.fromLeft_((f.x.max(0).min(layoutWidth - view.width)), layoutWidth);
					view.fromTop_((f.y.max(0).min(layoutHeight - view.height)), layoutHeight);
					this.updateResizeHandles;
				})
			},{
			//	if(gridOn,{ point = point.round(gridStep); });
				selectedViews.first.bounds_(Rect.fromPoints(point,resizeFixed),
					layoutWidth, layoutHeight);
				this.updateResizeHandles;
			});
		},
		{
			if (selection.notNil, {
				selection.mouseDrag(point);
				selectionChanged = true;
			});
		});
		userView.refresh;
	}

	updateResizeHandles { var r,d=4;
		resizeHandles = if( selectedViews.size == 1,{
			dragging = true;
			r = selectedViews.first.bounds(layoutWidth, layoutHeight);
			[ r.leftTop, r.rightTop, r.rightBottom, r.leftBottom ]
				.collect({ |center| Rect.aboutPoint(center,d,d) })
		});
		userView.refresh
	}
	setResizeFixed { |resizeHandle|
		var r, i;
		r = selectedViews.first.bounds(layoutWidth, layoutHeight);
		i = resizeHandles.indexOf(resizeHandle);
		resizeFixed = r.perform([ \rightBottom, \leftBottom, \leftTop, \rightTop ][i])
	}

	viewContainingPoint { |point|
		if (point.x.isNil, {^nil});
		if (point.y.isNil, {^nil});
		arrWidgets.reverseDo({ |view|
			var viewRect;
			viewRect = Rect(view.fromLeft(layoutWidth), view.fromTop(layoutHeight),
				view.width, view.height);
			if (viewRect.containsPoint(point),
				{ ^view });
		});
		^nil
	}
	highlightView{ arg view;
		if (view.highlight == false, {
			this.unhighlightAllViews;
			view.highlight = true;
			if (highlightActionFunc.notNil, {
				highlightActionFunc.value(view);
			});
		});
	}
	highlightSelectedViews {
		selectedViews.do({ |view|
			view.highlight = true;
		});
		if (highlightActionFunc.notNil, {
			highlightActionFunc.value(selectedViews.first);
		});
	}
	unhighlightAllViews {
		arrWidgets.do({ |view|
			view.highlight = false;
		})
	}
	fitToGridSelectedViews {
		selectedViews.do({ |view|
			view.width_(view.width(layoutWidth).round(gridStep).max(gridStep), layoutWidth);
			view.height_(view.height(layoutHeight).round(gridStep).max(gridStep), layoutHeight);
			view.fromLeft_(view.fromLeft(layoutWidth).round(gridStep), layoutWidth);
			view.fromTop_(view.fromTop(layoutHeight).round(gridStep), layoutHeight);
		})
	}
	drawGrid {
		// draw grid lines if gridStep > 1
		if (gridStep > 1, {

			Pen.use {
				Pen.strokeColor = Color.white.alpha_(0.4);
				Pen.width = 1;
				layoutHeight.do({ |y|
					if (y % gridStep == 0, {
						// draw line
						Pen.moveTo(0 @ y);
						Pen.lineTo(layoutWidth @ y);
					});
				});
				layoutWidth.do({ |x|
					if (x % gridStep == 0, {
						// draw line
						Pen.moveTo(x @ 0);
						Pen.lineTo(x @ layoutHeight);
					});
				});
				Pen.stroke;
			};
		});
	}
}

TXAreaSelection
{
	var click,round,<rect;

	*new { |p,r| r !? { p = round(p,r) };
		^super.newCopyArgs(p,r).mouseDrag(p)
	}

	mouseDrag { |drag|
		round !? { drag = round(drag,round) };
		rect = Rect.fromPoints(click,drag)
	}

	selects { |aRect|
		^rect.intersects(aRect)
	}
}
