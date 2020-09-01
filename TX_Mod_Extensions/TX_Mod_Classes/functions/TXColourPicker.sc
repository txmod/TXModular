// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// NOTE : Original code is:
// SCColorChooser by scsolar 10.2007
// see:    http://supercollider.sourceforge.net/wiki/index.php/ColorPicker
// Modified for TX Modular System by Paul Miller - 2010/ 2013/ 2016/ 2017

TXColourPicker {

	classvar classData;
	*initClass{
		classData = ();
		classData.alwaysOnTop = 1;
		classData.arrColourSlots = Color.white ! 25;
		classData.holdColour = Color.white;
	}

	*showPicker {
		var res = 20, scrsize = 240, set, holdView, valSlider;

		if (classData.window.isNil, {
			classData.window = Window.new(
				"Colour Picker - select on left, adjust on right, drag from middle",
				Rect(400, 800, (2 * scrsize) + 140, scrsize),
				false
			).front;
			classData.window.view.background_(Color.white);
			classData.window.alwaysOnTop = classData.alwaysOnTop.asBoolean;
			classData.window.onClose_({
				classData.window = nil;
				this.clearViewData;
			});
			valSlider = Slider(classData.window, Rect(scrsize, 0, 20, scrsize))
				.value_(1)
				.action_({
					classData.window.refresh;
				});
			classData.arrSliders = ["H", "S", "B", "R", "G", "B"].collect({arg item, i;
				StaticText(classData.window, Rect((2 * scrsize) + 20 + (i * 20), 0, 20, 20))
				.string_(item).align_('center')
				.stringColor_(Color.black).background_(Color.white);
				Slider(classData.window, Rect((2 * scrsize) + 20 + (i * 20), 20, 20, scrsize - 20))
				.value_(1)
				.background_(((Color.grey(0.65) ! 3) ++ [Color.red, Color.green, Color.blue])[i])
				.action_({
					if (i < 3, {
						this.setColorFromHSV(classData.arrSliders[0].value,
							classData.arrSliders[1].value, classData.arrSliders[2].value);
						this.setRGBSliders;
					}, {
						this.setColorFromRGB(classData.arrSliders[3].value,
							classData.arrSliders[4].value, classData.arrSliders[5].value);
						this.setHSVSliders;
					});
				});
			});

			classData.coloursBox = UserView(classData.window, Rect(0, 0, scrsize, scrsize))
			.mouseDownAction_({arg view, x, y;
				var col, sRed, sRed255, sGreen, sGreen255, sBlue, sBlue255;
				x = x.clip(0,scrsize);
				y = y.clip(0,scrsize);
				col = Color.hsv(min(0.999, x/scrsize), min(0.999, (y/scrsize)), valSlider.value, 1);
				this.updateMainColour(col);
				this.setHSVSliders;
				this.setRGBSliders;
			});
			classData.coloursBox.mouseMoveAction = classData.coloursBox.mouseDownAction;

			classData.window.drawFunc = {
				res.do({ arg i;
					res.do({ arg j;
						Color.hsv(1/res*i,1/res*j, valSlider.value, 1).set;
						Pen.fillRect(Rect((scrsize/res)*i, (scrsize/res)*j, (scrsize/res),
							(scrsize/res)));
					})
				})
			};
			classData.arrColourSlots.do({ arg item, i;
				var scr, posX, posY, width, height;

				if (i == 0, {
					posX = scrsize+20;
					posY = 0;
					width = scrsize;
					height = scrsize - 41;
				}, {
					if (i <= 12, {
						posX = scrsize+20 + ((i-1) * 20);
						posY = scrsize - 40;
					}, {
						posX = scrsize+20 + ((i-13) * 20);
						posY = scrsize - 20;
					});
					width = 18;
					height = 18;
				});
				scr = DragBoth.new(classData.window,Rect(posX, posY, width, height));
				scr.background_(item);
				scr.align_('center');
				scr.beginDragAction_({ arg view, x, y;
					var holdColour;
					view.dragLabel_("Colour");
					holdColour = scr.background;
					// return colour
					holdColour;
				});
				scr.canReceiveDragHandler = {
					View.currentDrag.isKindOf( Color )
				};
				scr.receiveDragHandler = {
					var holdDragObject;
					holdDragObject = View.currentDrag;
					scr.background = holdDragObject;
					classData.arrColourSlots[i] = holdDragObject;
					if (i == 0, {
						this.updateMainColour(holdDragObject);
						this.setHSVSliders;
						this.setRGBSliders;
					});
				};
				if (i == 0, {
					scr.setBoth = false;
				});
				// add scr to array
				classData.arrColBoxes = classData.arrColBoxes.add(scr);
			});
			this.updateMainColour(classData.holdColour);
			this.setHSVSliders;
			this.setRGBSliders;
			holdView = TXCheckBox(classData.window, Rect(scrsize + 20, 0, scrsize, 20),
				"window always on top", TXColor.grey(0.5), TXColour.white, TXColor.white, TXColor.grey(0.65));
			holdView.value =  classData.alwaysOnTop;
			holdView.action = {|view|
				classData.alwaysOnTop = view.value;
				classData.window.alwaysOnTop = classData.alwaysOnTop.asBoolean;
			};
		});
		classData.window.front;
	}

	*updateMainColour {arg argColour = Color.white;
		var holdBox, sRed, sRed255, sGreen, sGreen255, sBlue, sBlue255;
		classData.holdColour = argColour;
		holdBox = classData.arrColBoxes[0];
		if (holdBox.notNil, {
			holdBox.background = argColour;
			sRed = argColour.red.round(0.01);
			sRed255 = (argColour.red * 255).round(1);
			sGreen = argColour.green.round(0.01);
			sGreen255 = (argColour.green * 255).round(1);
			sBlue = argColour.blue.round(0.01);
			sBlue255 = (argColour.blue * 255).round(1);
			if (argColour.lightness < 0.6, {holdBox.stringColor_(Color.white)},
				{holdBox.stringColor_(Color.black)});
			holdBox.string = "R " ++ sRed ++ "/" ++ sRed255
			++ "   G " ++ sGreen ++ "/" ++ sGreen255
			++ "   B " ++ sBlue ++ "/" ++ sBlue255;
		});
	}

	*setColorFromHSV {arg hx, sx, vx;
		classData.holdColour = Color.hsv(hx.min(0.99999), sx, vx);
		this.updateMainColour(classData.holdColour);
	}

	*setColorFromRGB {arg rx, gx, bx;
		classData.holdColour = Color.fromArray([rx, gx, bx]);
		this.updateMainColour(classData.holdColour);
	}

	*setRGBSliders {
		var holdColourArray = classData.holdColour.asArray;
		classData.arrSliders[3].value = holdColourArray[0];
		classData.arrSliders[4].value = holdColourArray[1];
		classData.arrSliders[5].value = holdColourArray[2];
	}

	*setHSVSliders {
		var holdHSVColour = classData.holdColour.asHSV;
		classData.arrSliders[0].value = holdHSVColour[0]; // hue
		classData.arrSliders[1].value = holdHSVColour[1]; // sat
		classData.arrSliders[2].value = holdHSVColour[2]; // val
	}

	*closePickerWindow {
		if (classData.window.notNil, {
			classData.window.close;
		});
	}

	*clearViewData {
		classData.arrSliders = nil;
		classData.arrColBoxes = nil;
		classData.coloursBox = nil;
	}

}
