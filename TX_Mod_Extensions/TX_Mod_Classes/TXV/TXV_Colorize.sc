// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Colorize : TXV_Module {
	classvar <defaultName = "TXV Colorize";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ... "band1SelectionChannel/fixedValue" and band2/3/4
	// add text array to ... "band1Mod1Channel/fixedValue" and band2/3/4 and mod2
	// , ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]


	// add text array to ... "band1Mod1Type/fixedValue" and band2/3/4 and mod2
	// , ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]


	// add text array to ... "band1Mod1Curve/fixedValue" and band2/3/4 and Mod2
	// , ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]

	// add text array to ... "band1Mod1PhaseBase/fixedValue" and band2/3/4 and mod2
	// , ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]

	// add text array to ... "blendMode/fixedValue"
	// , ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXColorize_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXColorize_1/drawActive", [
			["bool_int", "/TXColorize_1/drawActive/fixedValue", 1],
			["bool_int", "/TXColorize_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXColorize_1/drawActive/useExtMod", 0],
			["float", "/TXColorize_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXColorize_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXColorize_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXColorize_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXColorize_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXColorize_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXColorize_1/sourceImageScaleMode", [
			["int", "/TXColorize_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXColorize_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXColorize_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXColorize_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/renderContinuosly", [
			["bool_int", "/TXColorize_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXColorize_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXColorize_1/renderContinuosly/useExtMod", 0],
			["float", "/TXColorize_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/renderNow", [
			["bool_int", "/TXColorize_1/renderNow/fixedValue", 0],
			["bool_int", "/TXColorize_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXColorize_1/renderNow/useExtMod", 0],
			["float", "/TXColorize_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXColorize_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXColorize_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXColorize_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXColorize_1/clearBeforeRender", [
			["bool_int", "/TXColorize_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXColorize_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXColorize_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXColorize_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/clearNow", [
			["bool_int", "/TXColorize_1/clearNow/fixedValue", 0],
			["bool_int", "/TXColorize_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXColorize_1/clearNow/useExtMod", 0],
			["float", "/TXColorize_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/clearColorHue", [
			["float", "/TXColorize_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/clearColorHue/useExtMod", 0],
			["float", "/TXColorize_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXColorize_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/clearColorSaturation", [
			["float", "/TXColorize_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXColorize_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXColorize_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXColorize_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/clearColorBrightness", [
			["float", "/TXColorize_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXColorize_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXColorize_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/clearColorAlpha", [
			["float", "/TXColorize_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXColorize_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXColorize_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXColorize_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXColorize_1/band1Level", [
			["float", "/TXColorize_1/band1Level/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Level/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Level/useExtMod", 0],
			["float", "/TXColorize_1/band1Level/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Level/softMin", 0, 0, 3],
			["float", "/TXColorize_1/band1Level/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1SelectionMin", [
			["float", "/TXColorize_1/band1SelectionMin/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1SelectionMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1SelectionMin/useExtMod", 0],
			["float", "/TXColorize_1/band1SelectionMin/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1SelectionMin/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1SelectionMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1SelectionMax", [
			["float", "/TXColorize_1/band1SelectionMax/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1SelectionMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1SelectionMax/useExtMod", 0],
			["float", "/TXColorize_1/band1SelectionMax/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1SelectionMax/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1SelectionMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1SelectionChannel", [
			["int", "/TXColorize_1/band1SelectionChannel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band1SelectionChannel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1SelectionChannel/useExtMod", 0],
			["float", "/TXColorize_1/band1SelectionChannel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1SelectionChannel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band1SelectionChannel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1TintMix", [
			["float", "/TXColorize_1/band1TintMix/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1TintMix/useExtMod", 0],
			["float", "/TXColorize_1/band1TintMix/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintMix/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1TintMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band1TintVaryBrightness", [
			["bool_int", "/TXColorize_1/band1TintVaryBrightness/fixedValue", 0],
			["bool_int", "/TXColorize_1/band1TintVaryBrightness/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band1TintVaryBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band1TintVaryBrightness/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1TintColorHue", [
			["float", "/TXColorize_1/band1TintColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band1TintColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1TintColorHue/useExtMod", 0],
			["float", "/TXColorize_1/band1TintColorHue/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorHue/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1TintColorHueRotate", [
			["float", "/TXColorize_1/band1TintColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1TintColorHueRotate/useExtMod", 0],
			["float", "/TXColorize_1/band1TintColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1TintColorSaturation", [
			["float", "/TXColorize_1/band1TintColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXColorize_1/band1TintColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1TintColorSaturation/useExtMod", 0],
			["float", "/TXColorize_1/band1TintColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorSaturation/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1TintColorBrightness", [
			["float", "/TXColorize_1/band1TintColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band1TintColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1TintColorBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band1TintColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorBrightness/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1TintColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band1TintColorHSBAsRGB", [
			["bool_int", "/TXColorize_1/band1TintColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXColorize_1/band1TintColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band1TintColorHSBAsRGB/useExtMod", 0],
			["float", "/TXColorize_1/band1TintColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod1Type", [
			["int", "/TXColorize_1/band1Mod1Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band1Mod1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod1Type/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod1Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod1Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band1Mod1Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod1Channel", [
			["int", "/TXColorize_1/band1Mod1Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band1Mod1Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod1Channel/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod1Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod1Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band1Mod1Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod1Curve", [
			["int", "/TXColorize_1/band1Mod1Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band1Mod1Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod1Curve/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod1Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod1Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band1Mod1Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1Mod1Freq", [
			["float", "/TXColorize_1/band1Mod1Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band1Mod1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod1Freq/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod1Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod1Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band1Mod1Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod1PhaseBase", [
			["int", "/TXColorize_1/band1Mod1PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band1Mod1PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod1PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod1PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod1PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band1Mod1PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1Mod1PhaseOffset", [
			["float", "/TXColorize_1/band1Mod1PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod1PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod1PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod1PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod1PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod1PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod2Type", [
			["int", "/TXColorize_1/band1Mod2Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band1Mod2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod2Type/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod2Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod2Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band1Mod2Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod2Channel", [
			["int", "/TXColorize_1/band1Mod2Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band1Mod2Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod2Channel/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod2Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod2Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band1Mod2Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod2Curve", [
			["int", "/TXColorize_1/band1Mod2Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band1Mod2Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod2Curve/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod2Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod2Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band1Mod2Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1Mod2Freq", [
			["float", "/TXColorize_1/band1Mod2Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band1Mod2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod2Freq/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod2Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod2Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band1Mod2Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band1Mod2PhaseBase", [
			["int", "/TXColorize_1/band1Mod2PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band1Mod2PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod2PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod2PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band1Mod2PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band1Mod2PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band1Mod2PhaseOffset", [
			["float", "/TXColorize_1/band1Mod2PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod2PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band1Mod2PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band1Mod2PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod2PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band1Mod2PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2Level", [
			["float", "/TXColorize_1/band2Level/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Level/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Level/useExtMod", 0],
			["float", "/TXColorize_1/band2Level/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Level/softMin", 0, 0, 3],
			["float", "/TXColorize_1/band2Level/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2SelectionMin", [
			["float", "/TXColorize_1/band2SelectionMin/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2SelectionMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2SelectionMin/useExtMod", 0],
			["float", "/TXColorize_1/band2SelectionMin/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2SelectionMin/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2SelectionMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2SelectionMax", [
			["float", "/TXColorize_1/band2SelectionMax/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2SelectionMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2SelectionMax/useExtMod", 0],
			["float", "/TXColorize_1/band2SelectionMax/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2SelectionMax/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2SelectionMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2SelectionChannel", [
			["int", "/TXColorize_1/band2SelectionChannel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band2SelectionChannel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2SelectionChannel/useExtMod", 0],
			["float", "/TXColorize_1/band2SelectionChannel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2SelectionChannel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band2SelectionChannel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2TintMix", [
			["float", "/TXColorize_1/band2TintMix/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2TintMix/useExtMod", 0],
			["float", "/TXColorize_1/band2TintMix/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintMix/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2TintMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band2TintVaryBrightness", [
			["bool_int", "/TXColorize_1/band2TintVaryBrightness/fixedValue", 0],
			["bool_int", "/TXColorize_1/band2TintVaryBrightness/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band2TintVaryBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band2TintVaryBrightness/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2TintColorHue", [
			["float", "/TXColorize_1/band2TintColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band2TintColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2TintColorHue/useExtMod", 0],
			["float", "/TXColorize_1/band2TintColorHue/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorHue/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2TintColorHueRotate", [
			["float", "/TXColorize_1/band2TintColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2TintColorHueRotate/useExtMod", 0],
			["float", "/TXColorize_1/band2TintColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2TintColorSaturation", [
			["float", "/TXColorize_1/band2TintColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXColorize_1/band2TintColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2TintColorSaturation/useExtMod", 0],
			["float", "/TXColorize_1/band2TintColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorSaturation/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2TintColorBrightness", [
			["float", "/TXColorize_1/band2TintColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band2TintColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2TintColorBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band2TintColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorBrightness/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2TintColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band2TintColorHSBAsRGB", [
			["bool_int", "/TXColorize_1/band2TintColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXColorize_1/band2TintColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band2TintColorHSBAsRGB/useExtMod", 0],
			["float", "/TXColorize_1/band2TintColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod1Type", [
			["int", "/TXColorize_1/band2Mod1Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band2Mod1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod1Type/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod1Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod1Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band2Mod1Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod1Channel", [
			["int", "/TXColorize_1/band2Mod1Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band2Mod1Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod1Channel/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod1Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod1Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band2Mod1Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod1Curve", [
			["int", "/TXColorize_1/band2Mod1Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band2Mod1Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod1Curve/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod1Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod1Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band2Mod1Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2Mod1Freq", [
			["float", "/TXColorize_1/band2Mod1Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band2Mod1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod1Freq/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod1Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod1Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band2Mod1Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod1PhaseBase", [
			["int", "/TXColorize_1/band2Mod1PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band2Mod1PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod1PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod1PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod1PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band2Mod1PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2Mod1PhaseOffset", [
			["float", "/TXColorize_1/band2Mod1PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod1PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod1PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod1PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod1PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod1PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod2Type", [
			["int", "/TXColorize_1/band2Mod2Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band2Mod2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod2Type/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod2Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod2Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band2Mod2Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod2Channel", [
			["int", "/TXColorize_1/band2Mod2Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band2Mod2Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod2Channel/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod2Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod2Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band2Mod2Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod2Curve", [
			["int", "/TXColorize_1/band2Mod2Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band2Mod2Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod2Curve/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod2Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod2Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band2Mod2Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2Mod2Freq", [
			["float", "/TXColorize_1/band2Mod2Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band2Mod2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod2Freq/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod2Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod2Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band2Mod2Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band2Mod2PhaseBase", [
			["int", "/TXColorize_1/band2Mod2PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band2Mod2PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod2PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod2PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band2Mod2PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band2Mod2PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band2Mod2PhaseOffset", [
			["float", "/TXColorize_1/band2Mod2PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod2PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band2Mod2PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band2Mod2PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod2PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band2Mod2PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3Level", [
			["float", "/TXColorize_1/band3Level/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Level/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Level/useExtMod", 0],
			["float", "/TXColorize_1/band3Level/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Level/softMin", 0, 0, 3],
			["float", "/TXColorize_1/band3Level/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3SelectionMin", [
			["float", "/TXColorize_1/band3SelectionMin/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3SelectionMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3SelectionMin/useExtMod", 0],
			["float", "/TXColorize_1/band3SelectionMin/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3SelectionMin/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3SelectionMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3SelectionMax", [
			["float", "/TXColorize_1/band3SelectionMax/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3SelectionMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3SelectionMax/useExtMod", 0],
			["float", "/TXColorize_1/band3SelectionMax/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3SelectionMax/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3SelectionMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3SelectionChannel", [
			["int", "/TXColorize_1/band3SelectionChannel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band3SelectionChannel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3SelectionChannel/useExtMod", 0],
			["float", "/TXColorize_1/band3SelectionChannel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3SelectionChannel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band3SelectionChannel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3TintMix", [
			["float", "/TXColorize_1/band3TintMix/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3TintMix/useExtMod", 0],
			["float", "/TXColorize_1/band3TintMix/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintMix/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3TintMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band3TintVaryBrightness", [
			["bool_int", "/TXColorize_1/band3TintVaryBrightness/fixedValue", 0],
			["bool_int", "/TXColorize_1/band3TintVaryBrightness/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band3TintVaryBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band3TintVaryBrightness/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3TintColorHue", [
			["float", "/TXColorize_1/band3TintColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band3TintColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3TintColorHue/useExtMod", 0],
			["float", "/TXColorize_1/band3TintColorHue/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorHue/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3TintColorHueRotate", [
			["float", "/TXColorize_1/band3TintColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3TintColorHueRotate/useExtMod", 0],
			["float", "/TXColorize_1/band3TintColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3TintColorSaturation", [
			["float", "/TXColorize_1/band3TintColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXColorize_1/band3TintColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3TintColorSaturation/useExtMod", 0],
			["float", "/TXColorize_1/band3TintColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorSaturation/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3TintColorBrightness", [
			["float", "/TXColorize_1/band3TintColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band3TintColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3TintColorBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band3TintColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorBrightness/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3TintColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band3TintColorHSBAsRGB", [
			["bool_int", "/TXColorize_1/band3TintColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXColorize_1/band3TintColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band3TintColorHSBAsRGB/useExtMod", 0],
			["float", "/TXColorize_1/band3TintColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod1Type", [
			["int", "/TXColorize_1/band3Mod1Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band3Mod1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod1Type/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod1Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod1Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band3Mod1Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod1Channel", [
			["int", "/TXColorize_1/band3Mod1Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band3Mod1Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod1Channel/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod1Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod1Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band3Mod1Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod1Curve", [
			["int", "/TXColorize_1/band3Mod1Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band3Mod1Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod1Curve/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod1Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod1Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band3Mod1Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3Mod1Freq", [
			["float", "/TXColorize_1/band3Mod1Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band3Mod1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod1Freq/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod1Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod1Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band3Mod1Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod1PhaseBase", [
			["int", "/TXColorize_1/band3Mod1PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band3Mod1PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod1PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod1PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod1PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band3Mod1PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3Mod1PhaseOffset", [
			["float", "/TXColorize_1/band3Mod1PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod1PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod1PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod1PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod1PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod1PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod2Type", [
			["int", "/TXColorize_1/band3Mod2Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band3Mod2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod2Type/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod2Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod2Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band3Mod2Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod2Channel", [
			["int", "/TXColorize_1/band3Mod2Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band3Mod2Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod2Channel/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod2Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod2Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band3Mod2Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod2Curve", [
			["int", "/TXColorize_1/band3Mod2Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band3Mod2Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod2Curve/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod2Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod2Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band3Mod2Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3Mod2Freq", [
			["float", "/TXColorize_1/band3Mod2Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band3Mod2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod2Freq/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod2Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod2Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band3Mod2Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band3Mod2PhaseBase", [
			["int", "/TXColorize_1/band3Mod2PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band3Mod2PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod2PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod2PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band3Mod2PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band3Mod2PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band3Mod2PhaseOffset", [
			["float", "/TXColorize_1/band3Mod2PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod2PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band3Mod2PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band3Mod2PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod2PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band3Mod2PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4Level", [
			["float", "/TXColorize_1/band4Level/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Level/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Level/useExtMod", 0],
			["float", "/TXColorize_1/band4Level/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Level/softMin", 0, 0, 3],
			["float", "/TXColorize_1/band4Level/softMax", 1, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4SelectionMin", [
			["float", "/TXColorize_1/band4SelectionMin/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4SelectionMin/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4SelectionMin/useExtMod", 0],
			["float", "/TXColorize_1/band4SelectionMin/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4SelectionMin/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4SelectionMin/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4SelectionMax", [
			["float", "/TXColorize_1/band4SelectionMax/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4SelectionMax/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4SelectionMax/useExtMod", 0],
			["float", "/TXColorize_1/band4SelectionMax/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4SelectionMax/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4SelectionMax/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4SelectionChannel", [
			["int", "/TXColorize_1/band4SelectionChannel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band4SelectionChannel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4SelectionChannel/useExtMod", 0],
			["float", "/TXColorize_1/band4SelectionChannel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4SelectionChannel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band4SelectionChannel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4TintMix", [
			["float", "/TXColorize_1/band4TintMix/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4TintMix/useExtMod", 0],
			["float", "/TXColorize_1/band4TintMix/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintMix/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4TintMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band4TintVaryBrightness", [
			["bool_int", "/TXColorize_1/band4TintVaryBrightness/fixedValue", 0],
			["bool_int", "/TXColorize_1/band4TintVaryBrightness/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band4TintVaryBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band4TintVaryBrightness/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4TintColorHue", [
			["float", "/TXColorize_1/band4TintColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band4TintColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4TintColorHue/useExtMod", 0],
			["float", "/TXColorize_1/band4TintColorHue/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorHue/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4TintColorHueRotate", [
			["float", "/TXColorize_1/band4TintColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4TintColorHueRotate/useExtMod", 0],
			["float", "/TXColorize_1/band4TintColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4TintColorSaturation", [
			["float", "/TXColorize_1/band4TintColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXColorize_1/band4TintColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4TintColorSaturation/useExtMod", 0],
			["float", "/TXColorize_1/band4TintColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorSaturation/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4TintColorBrightness", [
			["float", "/TXColorize_1/band4TintColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/band4TintColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4TintColorBrightness/useExtMod", 0],
			["float", "/TXColorize_1/band4TintColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorBrightness/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4TintColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/band4TintColorHSBAsRGB", [
			["bool_int", "/TXColorize_1/band4TintColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXColorize_1/band4TintColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXColorize_1/band4TintColorHSBAsRGB/useExtMod", 0],
			["float", "/TXColorize_1/band4TintColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod1Type", [
			["int", "/TXColorize_1/band4Mod1Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band4Mod1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod1Type/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod1Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod1Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band4Mod1Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod1Channel", [
			["int", "/TXColorize_1/band4Mod1Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band4Mod1Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod1Channel/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod1Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod1Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band4Mod1Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod1Curve", [
			["int", "/TXColorize_1/band4Mod1Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band4Mod1Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod1Curve/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod1Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod1Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band4Mod1Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4Mod1Freq", [
			["float", "/TXColorize_1/band4Mod1Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band4Mod1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod1Freq/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod1Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod1Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band4Mod1Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod1PhaseBase", [
			["int", "/TXColorize_1/band4Mod1PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band4Mod1PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod1PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod1PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod1PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band4Mod1PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4Mod1PhaseOffset", [
			["float", "/TXColorize_1/band4Mod1PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod1PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod1PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod1PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod1PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod1PhaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod2Type", [
			["int", "/TXColorize_1/band4Mod2Type/fixedValue", 0, 0, 6, ["Unchanged", "Invert channel", "Replace with value", "Add value", "Subtract value", "Multiply by value", "Divide by value"]],
			["float", "/TXColorize_1/band4Mod2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod2Type/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod2Type/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod2Type/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band4Mod2Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod2Channel", [
			["int", "/TXColorize_1/band4Mod2Channel/fixedValue", 5, 0, 5, ["Red Channel", "Green Channel", "Blue Channel", "Hue", "Saturation", "Luminosity"]],
			["float", "/TXColorize_1/band4Mod2Channel/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod2Channel/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod2Channel/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod2Channel/softMin", 0, 0, 5],
			["int", "/TXColorize_1/band4Mod2Channel/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod2Curve", [
			["int", "/TXColorize_1/band4Mod2Curve/fixedValue", 0, 0, 6, ["Curve A", "Curve B", "Curve C", "Curve D", "Ramp Up", "Ramp Down", "Ramp Up/Down",]],
			["float", "/TXColorize_1/band4Mod2Curve/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod2Curve/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod2Curve/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod2Curve/softMin", 0, 0, 6],
			["int", "/TXColorize_1/band4Mod2Curve/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4Mod2Freq", [
			["float", "/TXColorize_1/band4Mod2Freq/fixedValue", 1, 0.1, 10],
			["float", "/TXColorize_1/band4Mod2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod2Freq/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod2Freq/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod2Freq/softMin", 0.1, 0.01, 100],
			["float", "/TXColorize_1/band4Mod2Freq/softMax", 10, 0.01, 100],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/band4Mod2PhaseBase", [
			["int", "/TXColorize_1/band4Mod2PhaseBase/fixedValue", 0, 0, 3, ["Position within band normalised", "Position within band", "Perlin Noise", "Phase 0", ]],
			["float", "/TXColorize_1/band4Mod2PhaseBase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod2PhaseBase/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod2PhaseBase/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/band4Mod2PhaseBase/softMin", 0, 0, 3],
			["int", "/TXColorize_1/band4Mod2PhaseBase/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/band4Mod2PhaseOffset", [
			["float", "/TXColorize_1/band4Mod2PhaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod2PhaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/band4Mod2PhaseOffset/useExtMod", 0],
			["float", "/TXColorize_1/band4Mod2PhaseOffset/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod2PhaseOffset/softMin", 0, 0, 1],
			["float", "/TXColorize_1/band4Mod2PhaseOffset/softMax", 1, 0, 1],
		]],
		["float256", "/TXColorize_1/curveA1", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXColorize_1/curveA2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXColorize_1/curveAMorph", [
			["float", "/TXColorize_1/curveAMorph/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/curveAMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/curveAMorph/useExtMod", 0],
			["float", "/TXColorize_1/curveAMorph/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/curveAMorph/softMin", 0, 0, 1],
			["float", "/TXColorize_1/curveAMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXColorize_1/curveB1", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXColorize_1/curveB2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXColorize_1/curveBMorph", [
			["float", "/TXColorize_1/curveBMorph/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/curveBMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/curveBMorph/useExtMod", 0],
			["float", "/TXColorize_1/curveBMorph/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/curveBMorph/softMin", 0, 0, 1],
			["float", "/TXColorize_1/curveBMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXColorize_1/curveC1", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXColorize_1/curveC2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXColorize_1/curveCMorph", [
			["float", "/TXColorize_1/curveCMorph/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/curveCMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/curveCMorph/useExtMod", 0],
			["float", "/TXColorize_1/curveCMorph/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/curveCMorph/softMin", 0, 0, 1],
			["float", "/TXColorize_1/curveCMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXColorize_1/curveD1", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXColorize_1/curveD2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXColorize_1/band1FadeCurve", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 ],
		["float256", "/TXColorize_1/band2FadeCurve", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 ],
		["float256", "/TXColorize_1/band3FadeCurve", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 ],
		["float256", "/TXColorize_1/band4FadeCurve", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 ],
		[ "modParameterGroupFloat", "/TXColorize_1/curveDMorph", [
			["float", "/TXColorize_1/curveDMorph/fixedValue", 0, 0, 1],
			["float", "/TXColorize_1/curveDMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/curveDMorph/useExtMod", 0],
			["float", "/TXColorize_1/curveDMorph/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/curveDMorph/softMin", 0, 0, 1],
			["float", "/TXColorize_1/curveDMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXColorize_1/blendMode", [
			["int", "/TXColorize_1/blendMode/fixedValue", 0, 0, 26, ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]],
			["float", "/TXColorize_1/blendMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/blendMode/useExtMod", 0],
			["float", "/TXColorize_1/blendMode/extModValue", 0, 0, 1],
			["int", "/TXColorize_1/blendMode/softMin", 0, 0, 26],
			["int", "/TXColorize_1/blendMode/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/blendMix", [
			["float", "/TXColorize_1/blendMix/fixedValue", 1, 0, 1],
			["float", "/TXColorize_1/blendMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/blendMix/useExtMod", 0],
			["float", "/TXColorize_1/blendMix/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/blendMix/softMin", 0, 0, 1],
			["float", "/TXColorize_1/blendMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColorize_1/blendReverse", [
			["bool_int", "/TXColorize_1/blendReverse/fixedValue", 0],
			["bool_int", "/TXColorize_1/blendReverse/fixedModMix", 0],
			["bool_int", "/TXColorize_1/blendReverse/useExtMod", 0],
			["float", "/TXColorize_1/blendReverse/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/drawAlpha", [
			["float", "/TXColorize_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXColorize_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/drawAlpha/useExtMod", 0],
			["float", "/TXColorize_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXColorize_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/drawPosX", [
			["float", "/TXColorize_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/drawPosX/useExtMod", 0],
			["float", "/TXColorize_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXColorize_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/drawPosY", [
			["float", "/TXColorize_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/drawPosY/useExtMod", 0],
			["float", "/TXColorize_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXColorize_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/drawWidth", [
			["float", "/TXColorize_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXColorize_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/drawWidth/useExtMod", 0],
			["float", "/TXColorize_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXColorize_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/drawHeight", [
			["float", "/TXColorize_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXColorize_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/drawHeight/useExtMod", 0],
			["float", "/TXColorize_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXColorize_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/anchorX", [
			["float", "/TXColorize_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/anchorX/useExtMod", 0],
			["float", "/TXColorize_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXColorize_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/anchorY", [
			["float", "/TXColorize_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXColorize_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/anchorY/useExtMod", 0],
			["float", "/TXColorize_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXColorize_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/rotate", [
			["float", "/TXColorize_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXColorize_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/rotate/useExtMod", 0],
			["float", "/TXColorize_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/rotate/softMin", -360, -360, 360],
			["float", "/TXColorize_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/rotateMultiply", [
			["float", "/TXColorize_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXColorize_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/rotateMultiply/useExtMod", 0],
			["float", "/TXColorize_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/rotateMultiply/softMin", 0, 0, 6],
			["float", "/TXColorize_1/rotateMultiply/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/scaleX", [
			["float", "/TXColorize_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXColorize_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/scaleX/useExtMod", 0],
			["float", "/TXColorize_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXColorize_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXColorize_1/scaleY", [
			["float", "/TXColorize_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXColorize_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorize_1/scaleY/useExtMod", 0],
			["float", "/TXColorize_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXColorize_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXColorize_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXColorize_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
 /* ---------- TXV Outputs list for: TXColorize_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Band 1$Selection", {arg parameterName;
				parameterName.containsi("band1")
				&& not(parameterName.containsi("band1Tint"))
				&& not(parameterName.containsi("band1Mod"))
				;}],
			["Band 1$Tint", {arg parameterName;
				parameterName.containsi("band1Tint")
				;}],
			["Band 1$Mod1", {arg parameterName;
				parameterName.containsi("band1Mod1")
				;}],
			["Band 1$Mod2", {arg parameterName;
				parameterName.containsi("band1Mod2")
				;}],
			["Band 2$Selection", {arg parameterName;
				parameterName.containsi("band2")
				&& not(parameterName.containsi("band2Tint"))
				&& not(parameterName.containsi("band2Mod"))
				;}],
			["Band 2$Tint", {arg parameterName;
				parameterName.containsi("band2Tint")
				;}],
			["Band 2$Mod1", {arg parameterName;
				parameterName.containsi("band2Mod1")
				;}],
			["Band 2$Mod2", {arg parameterName;
				parameterName.containsi("band2Mod2")
				;}],
			["Band 3$Selection", {arg parameterName;
				parameterName.containsi("band3")
				&& not(parameterName.containsi("band3Tint"))
				&& not(parameterName.containsi("band3Mod"))
				;}],
			["Band 3$Tint", {arg parameterName;
				parameterName.containsi("band3Tint")
				;}],
			["Band 3$Mod1", {arg parameterName;
				parameterName.containsi("band3Mod1")
				;}],
			["Band 3$Mod2", {arg parameterName;
				parameterName.containsi("band3Mod2")
				;}],
			["Band 4$Selection", {arg parameterName;
				parameterName.containsi("band4")
				&& not(parameterName.containsi("band4Tint"))
				&& not(parameterName.containsi("band4Mod"))
				;}],
			["Band 4$Tint", {arg parameterName;
				parameterName.containsi("band4Tint")
				;}],
			["Band 4$Mod1", {arg parameterName;
				parameterName.containsi("band4Mod1")
				;}],
			["Band 4$Mod2", {arg parameterName;
				parameterName.containsi("band4Mod2")
				;}],
			["Curves A-D$Curve A", {arg parameterName;
				parameterName.containsi("curveA")
				;}],
			["Curves A-D$Curve B", {arg parameterName;
				parameterName.containsi("curveB")
				;}],
			["Curves A-D$Curve C", {arg parameterName;
				parameterName.containsi("curveC")
				;}],
			["Curves A-D$Curve D", {arg parameterName;
				parameterName.containsi("curveD")
				;}],
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
					"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY",
					"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Image & Blend";
	}

	initExtraActions { // override
		// arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
	}

	extraSubModuleData { // override
		^[arrSlotData];
	}

	loadExtraSubModuleData {arg argData;  // override
		arrSlotData = argData[0];
	}

	getExtraSynthArgSpecs { //  override
		^[
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
	}

}


