// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXColour : Color {		// TX system colours

	//	NB: See bottom of Color help file for all the X-windows colors

	classvar <>sysMainWindow;
	classvar <sysInterface;
	classvar <sysChannelAudio;
	classvar <sysChannelControl;
	classvar <sysModuleName;
	classvar <sysSelectedModString;
	classvar <sysChannelHighlight;
	classvar <sysModuleWindow;
	classvar <sysGuiCol1;
	classvar <sysGuiCol2;
	classvar <sysGuiCol3;
	classvar <sysGuiCol4;
	classvar <sysDeleteCol;
	classvar <sysEditCol;
	classvar <sysHelpCol;
	classvar <sysInterfaceButton;
	classvar <sysLabelBackground;
	classvar <sysViewHighlight;
	classvar <>colourPickerWindow;
	classvar sysAlpha;
	classvar arrColourSlots;
	*initClass {
		arrColourSlots = Color.white ! 11;
		sysAlpha = 1;
		this.sysColourSet1;
	}
	*sysColourSet1 {  arg argAlpha = 1;
		sysAlpha = argAlpha;
		sysMainWindow = Color.new255(67, 105, 255).alpha_(argAlpha);
		sysInterface = Color.new255(99.5, 130, 250, 255).alpha_(argAlpha);
		sysChannelAudio = Color.new255(130, 180, 255).alpha_(argAlpha);
		//		sysChannelControl = this.bluegreen.alpha_(argAlpha);
		//		sysChannelControl = Color.new255(150, 181, 191).alpha_(argAlpha);
		//		sysChannelControl = Color.new255(130, 201, 255).alpha_(argAlpha);
		sysChannelControl = Color.new255(140, 190, 235).alpha_(argAlpha);
		//		sysChannelHighlight = Color.new255(190, 225, 255).alpha_(argAlpha);
		//		sysChannelHighlight = Color.new255(210, 235, 255).alpha_(argAlpha);
		sysChannelHighlight = Color.new255(210, 230, 255).alpha_(argAlpha);
		sysModuleWindow = sysChannelAudio.alpha_(argAlpha);
		//		sysModuleName =  Color.new255(245, 255, 200) .alpha_(argAlpha);
		sysModuleName =  Color.new255(245, 255, 240) .alpha_(argAlpha);
		//		sysSelectedModString =  Color.new255(245, 255, 100) .alpha_(argAlpha);
		sysSelectedModString =  Color.new255(250, 255, 120) .alpha_(argAlpha);
		//		sysDeleteCol = Color.new255(0, 0, 60).alpha_(argAlpha);
		//		sysDeleteCol = Color.new255(40, 40, 100).alpha_(argAlpha);
		sysDeleteCol = Color.new255(50, 50, 100).alpha_(argAlpha);
		sysEditCol = this.orange2.alpha_(argAlpha);
		sysHelpCol = this.purple.alpha_(argAlpha);
		//		sysGuiCol1 = Color.new255(0, 0, 170).alpha_(argAlpha);
		sysGuiCol1 = Color.new255(0, 0, 220).alpha_(argAlpha);
		//		sysGuiCol2 = Color.new255(0, 85, 114).alpha_(argAlpha);
		//		sysGuiCol2 = Color.new255(0, 95, 124).alpha_(argAlpha);
		//		sysGuiCol2 = Color.new255(0, 105, 134).alpha_(argAlpha);
		sysGuiCol2 = Color.new255(0, 110, 140).alpha_(argAlpha);
		sysGuiCol3 = this.orange2.alpha_(argAlpha);
		sysGuiCol4 = Color.new255(70, 120, 200).alpha_(argAlpha);
		sysInterfaceButton = Color.new255(70, 120, 200).alpha_(argAlpha);
		sysLabelBackground =  Color.new255(140, 190, 235).alpha_(argAlpha);
		//		sysViewHighlight =   Color.yellow.alpha_(argAlpha);
		//		sysViewHighlight =   Color.new255(76, 21, 165).alpha_(argAlpha);
		sysViewHighlight =   Color.new255(76, 168, 5).alpha_(argAlpha);
	}
	*sysColourSet2 {
		sysMainWindow = Color.new255(90, 90, 255);
		sysInterface = Color.new255(99.5, 130, 250, 255);
		sysChannelAudio = Color.new255(150, 150, 255);
		sysChannelControl = this.green;
		sysChannelHighlight = Color.new255(195, 225, 255);
		sysModuleWindow = sysChannelAudio;
		sysModuleName =  Color.new255(245, 255, 200) ;
		sysSelectedModString =  Color.new255(245, 255, 100) ;
		sysDeleteCol = Color.new255(255, 80, 80);
		sysEditCol = this.orange2;
		sysHelpCol = this.purple;
		sysGuiCol1 = this.blue;
		sysGuiCol2 = this.green1;
		sysGuiCol3 = this.orange;
		sysGuiCol4 = Color.new255(70, 120, 200);
		sysInterfaceButton = Color.new255(70, 120, 200);
		sysLabelBackground =  Color.new255(140, 190, 235);
		sysViewHighlight =   Color.new255(76, 21, 165);
	}
	*sysColourSet3 {
		sysMainWindow = Color.new255(90, 140, 255);
		sysInterface = Color.new255(99.5, 130, 250, 255);
		sysChannelAudio = Color.new255(150, 190, 255);
		sysChannelControl = this.green;
		sysChannelHighlight = Color.new255(195, 225, 255);
		sysModuleWindow = sysChannelAudio;
		sysModuleName =  Color.new255(245, 255, 200) ;
		sysSelectedModString =  Color.new255(245, 255, 100) ;
		sysDeleteCol = Color.new255(255, 80, 80);
		sysEditCol = this.orange2;
		sysHelpCol = this.purple;
		sysGuiCol1 = this.blue;
		sysGuiCol2 = this.green1;
		sysGuiCol3 = this.orange;
		sysGuiCol4 = Color.new255(70, 120, 200);
		sysInterfaceButton = Color.new255(70, 120, 200);
		sysLabelBackground =  Color.new255(140, 190, 235);
		sysViewHighlight =   Color.new255(76, 21, 165);
	}

	*blank { ^Color.new255(0, 0, 0,0).alpha_(sysAlpha) }

	*blue1 { ^Color.new255(0, 0, 139).alpha_(sysAlpha) }
	*blue2 { ^Color.new255(0, 178, 238).alpha_(sysAlpha) }
	*blue3 { ^Color.new255(0, 0, 155).alpha_(sysAlpha) }
	*blue4 { ^Color.new255(0, 134, 139).alpha_(sysAlpha) }
	*bluegreen { ^Color.new255(0, 114, 152).alpha_(sysAlpha) }
	*blue5 { ^Color.new255(0, 142, 190).alpha_(sysAlpha) }
	*brown { ^Color.new255(139, 69, 19).alpha_(sysAlpha) }
	*brown2 { ^Color.new255(189, 183, 107).alpha_(sysAlpha) }
	*olive { ^Color.new255(85, 107, 47).alpha_(sysAlpha) }
	*grey1 { ^Color.grey(0.2).alpha_(sysAlpha) }
	*grey2 { ^Color.grey(0.3).alpha_(sysAlpha) }
	*grey3 { ^Color.grey(0.4).alpha_(sysAlpha) }
	*grey4 { ^Color.grey(0.6).alpha_(sysAlpha) }
	*grey5 { ^Color.grey(0.7).alpha_(sysAlpha) }
	*grey6 { ^Color.grey(0.8).alpha_(sysAlpha) }
	*grey7 { ^Color.grey(0.9).alpha_(sysAlpha) }
	*violet { ^Color.new255(148, 0, 211).alpha_(sysAlpha) }
	*pink { ^Color.new255(238, 18, 137).alpha_(sysAlpha) }
	*purple { ^Color.new255(139, 10, 80).alpha_(sysAlpha) }
	*yellow { ^Color.yellow}
	*yellow2 { ^Color.new255(255, 215, 0).alpha_(sysAlpha) }
	*green1 { ^Color.new255(0, 205, 0).alpha_(sysAlpha) }
	*green { ^Color.new255(0, 139, 0).alpha_(sysAlpha) }
	*orange { ^Color.new255(255, 165, 0).alpha_(sysAlpha) }
	*orange2 { ^Color.new255(255, 69, 0).alpha_(sysAlpha) }
	*red3 { ^Color.new255(238, 0, 0).alpha_(sysAlpha) }
	*red2 { ^Color.new255(139, 0, 0).alpha_(sysAlpha) }
	*red { ^Color.red}
	*pink2 { ^Color.new255(238, 58, 140).alpha_(sysAlpha) }
	*pink3 { ^Color.new255(255, 99, 71).alpha_(sysAlpha) }
	*pink4 { ^Color.new255(250, 180, 180).alpha_(sysAlpha) }
	*paleBlue { ^Color.new255(180, 180, 250).alpha_(sysAlpha) }
	*paleBlue2 { ^Color.new255(228, 240, 255).alpha_(sysAlpha) }
	*paleGreen { ^Color.new255(152, 251, 152).alpha_(sysAlpha) }
	*paleTurquoise { ^Color.new255(175, 238, 238).alpha_(sysAlpha) }
	*paleVioletRed { ^Color.new255(219, 112, 147).alpha_(sysAlpha) }
	*paleYellow { ^Color.new255(250, 250, 190).alpha_(sysAlpha) }
	*paleYellow2 { ^Color.new255(250, 250, 100).alpha_(sysAlpha) }

	*colourNames {
		^[	"black", "blue", "blue1", "blue2", "blue3", "blue4", "blue5", "bluegreen",
			"brown", "brown2", "clear", "grey", "grey1", "grey2", "grey3", "grey4", "grey5",
			"grey6", "grey7", "green1", "green", "olive", "orange", "orange2",
			"paleBlue",  "paleGreen",  "paleTurquoise", "paleVioletRed", "paleYellow", "paleYellow2",
			"pink", "pink2", "pink3", "pink4", "purple", "red", "red2", "red3",
			"violet","white", "yellow", "yellow2",
	]}

	*showPicker {
		// Original code for this method: SCColorChooser by scsolar 10.2007
		// see:    http://supercollider.sourceforge.net/wiki/index.php/ColorPicker
		// Modified for TX by Paul Miller - 2010/ 2013
		var arrColourBoxes, res = 20, scrsize = 200, val, set, coloursBox;
		if (colourPickerWindow.isNil, {
			colourPickerWindow = Window.new(
				"Colour Picker - select on left,  then drag from right",
				Rect(400, 400, 2*scrsize+20, scrsize),
				false
			).front;
			colourPickerWindow.view.background_(Color.white);

			// NOTE: SC Bug - if alwaysOnTop_ then dragging colors doesn't work
	//		colourPickerWindow.alwaysOnTop_(true);

			colourPickerWindow.onClose_({this.colourPickerWindow = nil;});
			val = Slider(colourPickerWindow, Rect(scrsize, 0, 20, scrsize))
			//		.background_(TXColor.grey)
			.value_(1)
			.action_({colourPickerWindow.refresh});

			coloursBox = UserView(colourPickerWindow, Rect(0, 0, scrsize, scrsize))
			.mouseDownAction_({arg view, x, y;
				arrColourBoxes[0].background_(
					Color.hsv(min(0.999, x/scrsize), min(0.999, (y/scrsize)), val.value, 1));
			})
			// .mouseMoveAction_({arg view, x, y;
			// 	arrColourBoxes[0].background_(
			// 	Color.hsv(min(0.999, x/scrsize), min(0.999, (y/scrsize)), val.value, 1));
			// })
			;

			colourPickerWindow.drawHook = {
				res.do({ arg i;
					res.do({ arg j;
						Color.hsv(1/res*i,1/res*j, val.value, 1).set;
						Pen.fillRect(Rect((scrsize/res)*i, (scrsize/res)*j, (scrsize/res),
							(scrsize/res)));
					})
				})
			};
			arrColourSlots.do({ arg item, i;
				var scr, posX, posY, width, height;
				if (i == 0, {
					posX = scrsize+20;
					posY = 0;
					width = scrsize;
					height = scrsize - 21;
					}, {
						posX = scrsize+20 + ((i-1) * 20);
						posY = scrsize - 20;
						width = 18;
						height = 18;
				});
				scr = DragBoth.new(colourPickerWindow,Rect(posX, posY, width, height));
				scr.background_(item);
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

					scr.background_(holdDragObject);
					arrColourSlots[i] = holdDragObject;
					colourPickerWindow.refresh;
				};
				// add scr to array
				arrColourBoxes = arrColourBoxes.add(scr);
			});
		});
		colourPickerWindow.front;
	}
}

TXColor : TXColour { // - allow for different spelling
}
