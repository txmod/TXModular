// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Levels : TXV_Module {
	classvar <defaultName = "TXV Levels";
	classvar <>arrInstances;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ... "levelsMode/fixedValue"     TXLEVELSMODE_RGB, TXLEVELSMODE_RED, TXLEVELSMODE_GREEN, TXLEVELSMODE_BLUE,  TXLEVELSMODE_REDGREEN, TXLEVELSMODE_REDBLUE, TXLEVELSMODE_GREENBLUE,
	// , ["RGB", "Red Only", "Green Only", "Blue Only", "Red & Green Only", "Red & Blue Only", "Green & Blue Only"]

	// add text array to ... "channelReorder/fixedValue"     TXLEVELSCHANNELORDER_RGB,TXLEVELSCHANNELORDER_RBG TXLEVELSCHANNELORDER_GBR, TXLEVELSCHANNELORDER_GRB, TXLEVELSCHANNELORDER_BRG, TXLEVELSCHANNELORDER_BGR,
	// , ["R G B (unchanged)", "R B G", "G B R", "G R B", "B R G", "B G R"]

	// add text array to ... "blendMode/fixedValue"
	// , ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXLevels_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		["bool_int", "/TXLevels_1/useExternalSourceImage", 0],
		[ "modParameterGroupBool", "/TXLevels_1/drawActive", [
			["bool_int", "/TXLevels_1/drawActive/fixedValue", 1],
			["bool_int", "/TXLevels_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXLevels_1/drawActive/useExtMod", 0],
			["float", "/TXLevels_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXLevels_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXLevels_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXLevels_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXLevels_1/sourceImageScaleMode", [
			["int", "/TXLevels_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXLevels_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXLevels_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXLevels_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXLevels_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXLevels_1/renderContinuosly", [
			["bool_int", "/TXLevels_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXLevels_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXLevels_1/renderContinuosly/useExtMod", 0],
			["float", "/TXLevels_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLevels_1/renderNow", [
			["bool_int", "/TXLevels_1/renderNow/fixedValue", 0],
			["bool_int", "/TXLevels_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXLevels_1/renderNow/useExtMod", 0],
			["float", "/TXLevels_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXLevels_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXLevels_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXLevels_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXLevels_1/clearBeforeRender", [
			["bool_int", "/TXLevels_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXLevels_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXLevels_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXLevels_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLevels_1/clearNow", [
			["bool_int", "/TXLevels_1/clearNow/fixedValue", 0],
			["bool_int", "/TXLevels_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXLevels_1/clearNow/useExtMod", 0],
			["float", "/TXLevels_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/clearColorHue", [
			["float", "/TXLevels_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXLevels_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/clearColorHue/useExtMod", 0],
			["float", "/TXLevels_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXLevels_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/clearColorSaturation", [
			["float", "/TXLevels_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXLevels_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXLevels_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXLevels_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/clearColorBrightness", [
			["float", "/TXLevels_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXLevels_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXLevels_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXLevels_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/clearColorAlpha", [
			["float", "/TXLevels_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXLevels_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXLevels_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXLevels_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXLevels_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupInt", "/TXLevels_1/levelsMode", [
			["int", "/TXLevels_1/levelsMode/fixedValue", 0, 0, 3, ["RGB", "Red Only", "Green Only", "Blue Only", "Red & Green Only", "Red & Blue Only", "Green & Blue Only"]],
			["float", "/TXLevels_1/levelsMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/levelsMode/useExtMod", 0],
			["float", "/TXLevels_1/levelsMode/extModValue", 0, 0, 1],
			["int", "/TXLevels_1/levelsMode/softMin", 0, 0, 6],
			["int", "/TXLevels_1/levelsMode/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/levelsMix", [
			["float", "/TXLevels_1/levelsMix/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/levelsMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/levelsMix/useExtMod", 0],
			["float", "/TXLevels_1/levelsMix/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/levelsMix/softMin", 0, 0, 1],
			["float", "/TXLevels_1/levelsMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/gamma", [
			["float", "/TXLevels_1/gamma/fixedValue", 1, 0.1, 10],
			["float", "/TXLevels_1/gamma/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/gamma/useExtMod", 0],
			["float", "/TXLevels_1/gamma/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/gamma/softMin", 0.1, 0.1, 10],
			["float", "/TXLevels_1/gamma/softMax", 10, 0.1, 10],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/inputMin", [
			["float", "/TXLevels_1/inputMin/fixedValue", 0, 0, 1],
			["float", "/TXLevels_1/inputMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/inputMin/useExtMod", 0],
			["float", "/TXLevels_1/inputMin/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/inputMin/softMin", 0, 0, 1],
			["float", "/TXLevels_1/inputMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/inputMax", [
			["float", "/TXLevels_1/inputMax/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/inputMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/inputMax/useExtMod", 0],
			["float", "/TXLevels_1/inputMax/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/inputMax/softMin", 0, 0, 1],
			["float", "/TXLevels_1/inputMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/outputMin", [
			["float", "/TXLevels_1/outputMin/fixedValue", 0, 0, 1],
			["float", "/TXLevels_1/outputMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/outputMin/useExtMod", 0],
			["float", "/TXLevels_1/outputMin/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/outputMin/softMin", 0, 0, 1],
			["float", "/TXLevels_1/outputMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/outputMax", [
			["float", "/TXLevels_1/outputMax/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/outputMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/outputMax/useExtMod", 0],
			["float", "/TXLevels_1/outputMax/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/outputMax/softMin", 0, 0, 1],
			["float", "/TXLevels_1/outputMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLevels_1/posterize", [
			["bool_int", "/TXLevels_1/posterize/fixedValue", 0],
			["bool_int", "/TXLevels_1/posterize/fixedModMix", 0],
			["bool_int", "/TXLevels_1/posterize/useExtMod", 0],
			["float", "/TXLevels_1/posterize/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXLevels_1/posterizeSteps", [
			["int", "/TXLevels_1/posterizeSteps/fixedValue", 3, 1, 10],
			["float", "/TXLevels_1/posterizeSteps/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/posterizeSteps/useExtMod", 0],
			["float", "/TXLevels_1/posterizeSteps/extModValue", 0, 0, 1],
			["int", "/TXLevels_1/posterizeSteps/softMin", 1, 1, 50],
			["int", "/TXLevels_1/posterizeSteps/softMax", 10, 1, 50],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/posterizeBias", [
			["float", "/TXLevels_1/posterizeBias/fixedValue", 1, 0.1, 10],
			["float", "/TXLevels_1/posterizeBias/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/posterizeBias/useExtMod", 0],
			["float", "/TXLevels_1/posterizeBias/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/posterizeBias/softMin", 0.1, 0.1, 10],
			["float", "/TXLevels_1/posterizeBias/softMax", 10, 0.1, 10],
		]],
		[ "modParameterGroupInt", "/TXLevels_1/channelReorder", [
			["int", "/TXLevels_1/channelReorder/fixedValue", 0, 0, 5, ["R G B (unchanged)", "R B G", "G B R", "G R B", "B R G", "B G R"]],
			["float", "/TXLevels_1/channelReorder/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/channelReorder/useExtMod", 0],
			["float", "/TXLevels_1/channelReorder/extModValue", 0, 0, 1],
			["int", "/TXLevels_1/channelReorder/softMin", 0, 0, 5],
			["int", "/TXLevels_1/channelReorder/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/redMultiply", [
			["float", "/TXLevels_1/redMultiply/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/redMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/redMultiply/useExtMod", 0],
			["float", "/TXLevels_1/redMultiply/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/redMultiply/softMin", 0, 0, 3],
			["float", "/TXLevels_1/redMultiply/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/greenMultiply", [
			["float", "/TXLevels_1/greenMultiply/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/greenMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/greenMultiply/useExtMod", 0],
			["float", "/TXLevels_1/greenMultiply/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/greenMultiply/softMin", 0, 0, 3],
			["float", "/TXLevels_1/greenMultiply/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/blueMultiply", [
			["float", "/TXLevels_1/blueMultiply/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/blueMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/blueMultiply/useExtMod", 0],
			["float", "/TXLevels_1/blueMultiply/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/blueMultiply/softMin", 0, 0, 3],
			["float", "/TXLevels_1/blueMultiply/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupInt", "/TXLevels_1/blendMode", [
			["int", "/TXLevels_1/blendMode/fixedValue", 0, 0, 26, ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]],
			["float", "/TXLevels_1/blendMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/blendMode/useExtMod", 0],
			["float", "/TXLevels_1/blendMode/extModValue", 0, 0, 1],
			["int", "/TXLevels_1/blendMode/softMin", 0, 0, 26],
			["int", "/TXLevels_1/blendMode/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/blendMix", [
			["float", "/TXLevels_1/blendMix/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/blendMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/blendMix/useExtMod", 0],
			["float", "/TXLevels_1/blendMix/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/blendMix/softMin", 0, 0, 1],
			["float", "/TXLevels_1/blendMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLevels_1/blendReverse", [
			["bool_int", "/TXLevels_1/blendReverse/fixedValue", 0],
			["bool_int", "/TXLevels_1/blendReverse/fixedModMix", 0],
			["bool_int", "/TXLevels_1/blendReverse/useExtMod", 0],
			["float", "/TXLevels_1/blendReverse/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/drawAlpha", [
			["float", "/TXLevels_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/drawAlpha/useExtMod", 0],
			["float", "/TXLevels_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXLevels_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/drawPosX", [
			["float", "/TXLevels_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXLevels_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/drawPosX/useExtMod", 0],
			["float", "/TXLevels_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXLevels_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/drawPosY", [
			["float", "/TXLevels_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXLevels_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/drawPosY/useExtMod", 0],
			["float", "/TXLevels_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXLevels_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/drawWidth", [
			["float", "/TXLevels_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/drawWidth/useExtMod", 0],
			["float", "/TXLevels_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXLevels_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/drawHeight", [
			["float", "/TXLevels_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXLevels_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/drawHeight/useExtMod", 0],
			["float", "/TXLevels_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXLevels_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/anchorX", [
			["float", "/TXLevels_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXLevels_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/anchorX/useExtMod", 0],
			["float", "/TXLevels_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXLevels_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/anchorY", [
			["float", "/TXLevels_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXLevels_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/anchorY/useExtMod", 0],
			["float", "/TXLevels_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXLevels_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/rotate", [
			["float", "/TXLevels_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXLevels_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/rotate/useExtMod", 0],
			["float", "/TXLevels_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/rotate/softMin", -360, -360, 360],
			["float", "/TXLevels_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/rotateMultiply", [
			["float", "/TXLevels_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXLevels_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/rotateMultiply/useExtMod", 0],
			["float", "/TXLevels_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXLevels_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/scaleX", [
			["float", "/TXLevels_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXLevels_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/scaleX/useExtMod", 0],
			["float", "/TXLevels_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXLevels_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXLevels_1/scaleY", [
			["float", "/TXLevels_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXLevels_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLevels_1/scaleY/useExtMod", 0],
			["float", "/TXLevels_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXLevels_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXLevels_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXLevels_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXLevels_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Levels", {arg parameterName;
				var arrNames = ["levelsMode", "levelsMix", "gamma", "inputMin", "inputMax", "outputMin", "outputMax", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Posterize", {arg parameterName;
				var arrNames = ["posterize", "posterizeSteps", "posterizeBias", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Channel Adjust", {arg parameterName;
				var arrNames = ["channelReorder", "redMultiply", "greenMultiply", "blueMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ",
					"useSamplePosForDrawPos","anchorX", "anchorY", "anchorZ", "rotate", "rotateX", "rotateY", "rotateZ",
					"rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Source Image & Blend";
	}

}


