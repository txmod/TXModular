// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXColour : Color {		// TX system colours

	//	NOTE 1: See bottom of Color help file for all the X-windows colors

	// NOTE 2: Default system colour set is sysColourSet2

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
	classvar <sysRecordCol;
	classvar <sysEditCol;
	classvar <sysHelpCol;
	classvar <sysInterfaceButton;
	classvar <sysLabelBackground;
	classvar <sysViewHighlight;
	classvar classData;
	*initClass {
		classData = ();
		classData.sysAlpha = 1;
		this.sysColourSet2;  // Default set is 2
	}
	*sysColourSet1 {  arg argAlpha = 1;
		sysGuiCol1 = Color.new255(0, 0, 220).alpha_(argAlpha);
		//		sysGuiCol2 = Color.new255(0, 85, 114).alpha_(argAlpha);
		//		sysGuiCol2 = Color.new255(0, 95, 124).alpha_(argAlpha);
		//		sysGuiCol2 = Color.new255(0, 105, 134).alpha_(argAlpha);
		sysGuiCol2 = Color.new255(0, 110, 140).alpha_(argAlpha);
		sysGuiCol3 = this.orange2.alpha_(argAlpha);
		sysGuiCol4 = Color.new255(70, 120, 200).alpha_(argAlpha);
		classData.sysAlpha = argAlpha;
		sysMainWindow = Color.new255(67, 105, 255).alpha_(argAlpha);
		sysInterface = Color.new255(99.5, 130, 250, 255).alpha_(argAlpha);
		//		sysGuiCol1 = Color.new255(0, 0, 170).alpha_(argAlpha);
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
		sysRecordCol = this.red.blend(TXColor.grey, 0.3);
		sysEditCol = this.orange2.alpha_(argAlpha);
		sysHelpCol = this.purple.alpha_(argAlpha);
		sysInterfaceButton = Color.new255(70, 120, 200).alpha_(argAlpha);
		sysLabelBackground =  Color.new255(140, 190, 235).alpha_(argAlpha);
		// sysViewHighlight =   Color.new255(76, 21, 165).alpha_(argAlpha);
		sysViewHighlight =   Color.new255(76, 168, 5).alpha_(argAlpha);
	}
	*sysColourSet2 {  arg argAlpha = 1;
		classData.sysAlpha = argAlpha;
		sysGuiCol1 = Color.new255(70, 120, 200).blend(Color.grey(0.35), 0.25).alpha_(argAlpha);
		// sysGuiCol2 = Color.new255(0, 110, 140).alpha_(argAlpha);
		//sysGuiCol2 = Color.new255(140, 180, 180).alpha_(argAlpha);
		//sysGuiCol2 = Color.new255(90, 140, 160).alpha_(argAlpha);
		sysGuiCol2 = Color.new255(90, 140, 160).blend(Color.grey(0.3), 0.25).alpha_(argAlpha);
		sysGuiCol3 = Color.new255(255, 169, 100).blend(Color.grey(0.65), 0.2).alpha_(argAlpha);
		//sysGuiCol4 = Color.new255(150, 200, 250).blend(Color.grey(0.6), 0.45).alpha_(argAlpha);
		sysGuiCol4 = Color.new255(70, 120, 200).blend(Color.grey(0.6), 0.7).alpha_(argAlpha);
		sysMainWindow = Color.grey(0.85).blend(sysGuiCol1, 0.1).alpha_(argAlpha);
		sysInterface = Color.new255(99.5, 130, 250, 255).alpha_(argAlpha);
		sysChannelAudio = Color.grey(0.83).blend(TXColor.sysGuiCol1, 0.3).alpha_(argAlpha);
		sysChannelControl = Color.grey(0.86).blend(TXColor.sysGuiCol2, 0.3).alpha_(argAlpha);
		sysChannelHighlight = Color.white.alpha_(argAlpha);
		sysModuleWindow = sysChannelAudio.alpha_(argAlpha);
		sysModuleName =  TXColor.paleYellow.alpha_(argAlpha);
		sysSelectedModString =  Color.new255(250, 255, 120) .alpha_(argAlpha);
		//sysDeleteCol = Color.new255(130, 135, 135).alpha_(argAlpha);
		sysDeleteCol = Color.new255(120, 127, 127).blend(Color.black, 0.1).alpha_(argAlpha);
		sysRecordCol = this.red.blend(TXColor.grey, 0.5);
		sysEditCol = sysGuiCol3;
		sysHelpCol = this.purple.blend(Color.grey(0.6), 0.6).alpha_(argAlpha);
		// sysGuiCol1 = Color.new255(0, 0, 220).alpha_(argAlpha);
		//sysGuiCol1 = Color.new255(150, 150, 190).alpha_(argAlpha);
		//sysGuiCol1 = Color.new255(70, 160, 200).alpha_(argAlpha);
		//sysGuiCol1 = Color.new255(70, 120, 200).alpha_(argAlpha);
		sysInterfaceButton = Color.new255(70, 120, 200).alpha_(argAlpha);
		//sysLabelBackground =  Color.new255(140, 190, 235).alpha_(argAlpha);
		//sysLabelBackground =  Color.grey(0.73).alpha_(argAlpha);
		sysLabelBackground =  Color.new255(164, 170, 175).blend(Color.black, 0.1).alpha_(argAlpha);
		// sysViewHighlight =   Color.new255(76, 168, 5).alpha_(argAlpha);
		sysViewHighlight =   Color.new255(255, 255, 190).alpha_(argAlpha);
	}

	*blank { ^Color.new255(0, 0, 0, 0) }

	*blue1 { ^Color.new255(0, 0, 139).alpha_(classData.sysAlpha) }
	*blue2 { ^Color.new255(0, 178, 238).alpha_(classData.sysAlpha) }
	*blue3 { ^Color.new255(0, 0, 155).alpha_(classData.sysAlpha) }
	*blue4 { ^Color.new255(0, 134, 139).alpha_(classData.sysAlpha) }
	*bluegreen { ^Color.new255(0, 114, 152).alpha_(classData.sysAlpha) }
	*blue5 { ^Color.new255(0, 142, 190).alpha_(classData.sysAlpha) }
	*brown { ^Color.new255(139, 69, 19).alpha_(classData.sysAlpha) }
	*brown2 { ^Color.new255(189, 183, 107).alpha_(classData.sysAlpha) }
	*olive { ^Color.new255(85, 107, 47).alpha_(classData.sysAlpha) }
	*grey1 { ^Color.grey(0.2).alpha_(classData.sysAlpha) }
	*grey2 { ^Color.grey(0.3).alpha_(classData.sysAlpha) }
	*grey3 { ^Color.grey(0.4).alpha_(classData.sysAlpha) }
	*grey4 { ^Color.grey(0.6).alpha_(classData.sysAlpha) }
	*grey5 { ^Color.grey(0.7).alpha_(classData.sysAlpha) }
	*grey6 { ^Color.grey(0.8).alpha_(classData.sysAlpha) }
	*grey7 { ^Color.grey(0.9).alpha_(classData.sysAlpha) }
	*violet { ^Color.new255(148, 0, 211).alpha_(classData.sysAlpha) }
	*pink { ^Color.new255(238, 18, 137).alpha_(classData.sysAlpha) }
	*purple { ^Color.new255(139, 10, 80).alpha_(classData.sysAlpha) }
	*yellow { ^Color.yellow}
	*yellow2 { ^Color.new255(255, 215, 0).alpha_(classData.sysAlpha) }
	*green1 { ^Color.new255(0, 205, 0).alpha_(classData.sysAlpha) }
	*green { ^Color.new255(0, 139, 0).alpha_(classData.sysAlpha) }
	*orange { ^Color.new255(255, 165, 0).alpha_(classData.sysAlpha) }
	*orange2 { ^Color.new255(255, 69, 0).alpha_(classData.sysAlpha) }
	*red3 { ^Color.new255(238, 0, 0).alpha_(classData.sysAlpha) }
	*red2 { ^Color.new255(139, 0, 0).alpha_(classData.sysAlpha) }
	*red { ^Color.red}
	*pink2 { ^Color.new255(238, 58, 140).alpha_(classData.sysAlpha) }
	*pink3 { ^Color.new255(255, 99, 71).alpha_(classData.sysAlpha) }
	*pink4 { ^Color.new255(250, 180, 180).alpha_(classData.sysAlpha) }
	*paleBlue { ^Color.new255(180, 180, 250).alpha_(classData.sysAlpha) }
	*paleBlue2 { ^Color.new255(228, 240, 255).alpha_(classData.sysAlpha) }
	*paleGreen { ^Color.new255(172, 255, 172).alpha_(classData.sysAlpha) }
	*paleGreen2 { ^Color.new255(236, 255, 236).alpha_(classData.sysAlpha) }
	*paleTurquoise { ^Color.new255(220, 255, 255).alpha_(classData.sysAlpha) }
	*paleVioletRed { ^Color.new255(255, 216, 229).alpha_(classData.sysAlpha) }
	*paleYellow { ^Color.new255(250, 250, 190).alpha_(classData.sysAlpha) }
	*paleYellow2 { ^Color.new255(250, 250, 120).alpha_(classData.sysAlpha) }

	*colourNames {
		^[	"black", "blue", "blue1", "blue2", "blue3", "blue4", "blue5", "bluegreen",
			"brown", "brown2", "clear", "grey", "grey1", "grey2", "grey3", "grey4", "grey5",
			"grey6", "grey7", "green1", "green", "olive", "orange", "orange2",
			"paleBlue",  "paleGreen",  "paleTurquoise", "paleVioletRed", "paleYellow", "paleYellow2",
			"pink", "pink2", "pink3", "pink4", "purple", "red", "red2", "red3",
			"violet","white", "yellow", "yellow2",
		];
	}

	*showPicker {
		TXColourPicker.showPicker;
	}

	*closePickerWindow {
		TXColourPicker.closePickerWindow;
	}

}

TXColor : TXColour { // - allow for different spelling
}
