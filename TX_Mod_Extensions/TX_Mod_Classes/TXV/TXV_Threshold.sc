// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Threshold : TXV_Module {
	classvar <defaultName = "TXV Threshold";
	classvar <>arrInstances;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ... "blendMode/fixedValue"
	// , ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXThreshold_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXThreshold_1/drawActive", [
			["bool_int", "/TXThreshold_1/drawActive/fixedValue", 1],
			["bool_int", "/TXThreshold_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/drawActive/useExtMod", 0],
			["float", "/TXThreshold_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXThreshold_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXThreshold_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXThreshold_1/customMaxHeight", 768, 1, 4096],
		["bool_int", "/TXThreshold_1/useExternalSourceImage", 0],
	[ "modParameterGroupInt", "/TXThreshold_1/sourceImageScaleMode", [
			["int", "/TXThreshold_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXThreshold_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXThreshold_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXThreshold_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXThreshold_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/renderContinuosly", [
			["bool_int", "/TXThreshold_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXThreshold_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/renderContinuosly/useExtMod", 0],
			["float", "/TXThreshold_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/renderNow", [
			["bool_int", "/TXThreshold_1/renderNow/fixedValue", 0],
			["bool_int", "/TXThreshold_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/renderNow/useExtMod", 0],
			["float", "/TXThreshold_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXThreshold_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXThreshold_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXThreshold_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXThreshold_1/clearBeforeRender", [
			["bool_int", "/TXThreshold_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXThreshold_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXThreshold_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/clearNow", [
			["bool_int", "/TXThreshold_1/clearNow/fixedValue", 0],
			["bool_int", "/TXThreshold_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/clearNow/useExtMod", 0],
			["float", "/TXThreshold_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/clearColorHue", [
			["float", "/TXThreshold_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXThreshold_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/clearColorHue/useExtMod", 0],
			["float", "/TXThreshold_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/clearColorSaturation", [
			["float", "/TXThreshold_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXThreshold_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXThreshold_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/clearColorBrightness", [
			["float", "/TXThreshold_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXThreshold_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/clearColorAlpha", [
			["float", "/TXThreshold_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXThreshold_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXThreshold_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXThreshold_1/threshold", [
			["float", "/TXThreshold_1/threshold/fixedValue", 0.5, 0, 1],
			["float", "/TXThreshold_1/threshold/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/threshold/useExtMod", 0],
			["float", "/TXThreshold_1/threshold/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/threshold/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/threshold/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/belowColorHue", [
			["float", "/TXThreshold_1/belowColorHue/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/belowColorHue/useExtMod", 0],
			["float", "/TXThreshold_1/belowColorHue/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorHue/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/belowColorSaturation", [
			["float", "/TXThreshold_1/belowColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/belowColorSaturation/useExtMod", 0],
			["float", "/TXThreshold_1/belowColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorSaturation/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/belowColorBrightness", [
			["float", "/TXThreshold_1/belowColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/belowColorBrightness/useExtMod", 0],
			["float", "/TXThreshold_1/belowColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorBrightness/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/belowColorAlpha", [
			["float", "/TXThreshold_1/belowColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/belowColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/belowColorAlpha/useExtMod", 0],
			["float", "/TXThreshold_1/belowColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorAlpha/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/belowColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/aboveColorHue", [
			["float", "/TXThreshold_1/aboveColorHue/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/aboveColorHue/useExtMod", 0],
			["float", "/TXThreshold_1/aboveColorHue/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorHue/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/aboveColorSaturation", [
			["float", "/TXThreshold_1/aboveColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/aboveColorSaturation/useExtMod", 0],
			["float", "/TXThreshold_1/aboveColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorSaturation/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/aboveColorBrightness", [
			["float", "/TXThreshold_1/aboveColorBrightness/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/aboveColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/aboveColorBrightness/useExtMod", 0],
			["float", "/TXThreshold_1/aboveColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorBrightness/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/aboveColorAlpha", [
			["float", "/TXThreshold_1/aboveColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/aboveColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/aboveColorAlpha/useExtMod", 0],
			["float", "/TXThreshold_1/aboveColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorAlpha/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/aboveColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/useSampledAboveColor", [
			["bool_int", "/TXThreshold_1/useSampledAboveColor/fixedValue", 0],
			["bool_int", "/TXThreshold_1/useSampledAboveColor/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/useSampledAboveColor/useExtMod", 0],
			["float", "/TXThreshold_1/useSampledAboveColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/useSampledBelowColor", [
			["bool_int", "/TXThreshold_1/useSampledBelowColor/fixedValue", 0],
			["bool_int", "/TXThreshold_1/useSampledBelowColor/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/useSampledBelowColor/useExtMod", 0],
			["float", "/TXThreshold_1/useSampledBelowColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/belowSamplePosX", [
			["float", "/TXThreshold_1/belowSamplePosX/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowSamplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/belowSamplePosX/useExtMod", 0],
			["float", "/TXThreshold_1/belowSamplePosX/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowSamplePosX/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/belowSamplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/belowSamplePosY", [
			["float", "/TXThreshold_1/belowSamplePosY/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowSamplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/belowSamplePosY/useExtMod", 0],
			["float", "/TXThreshold_1/belowSamplePosY/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/belowSamplePosY/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/belowSamplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/aboveSamplePosX", [
			["float", "/TXThreshold_1/aboveSamplePosX/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveSamplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/aboveSamplePosX/useExtMod", 0],
			["float", "/TXThreshold_1/aboveSamplePosX/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveSamplePosX/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/aboveSamplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/aboveSamplePosY", [
			["float", "/TXThreshold_1/aboveSamplePosY/fixedValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveSamplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/aboveSamplePosY/useExtMod", 0],
			["float", "/TXThreshold_1/aboveSamplePosY/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/aboveSamplePosY/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/aboveSamplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/swapColors", [
			["bool_int", "/TXThreshold_1/swapColors/fixedValue", 0],
			["bool_int", "/TXThreshold_1/swapColors/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/swapColors/useExtMod", 0],
			["float", "/TXThreshold_1/swapColors/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXThreshold_1/blendMode", [
			["int", "/TXThreshold_1/blendMode/fixedValue", 0, 0, 26, ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]],
			["float", "/TXThreshold_1/blendMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/blendMode/useExtMod", 0],
			["float", "/TXThreshold_1/blendMode/extModValue", 0, 0, 1],
			["int", "/TXThreshold_1/blendMode/softMin", 0, 0, 26],
			["int", "/TXThreshold_1/blendMode/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/blendMix", [
			["float", "/TXThreshold_1/blendMix/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/blendMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/blendMix/useExtMod", 0],
			["float", "/TXThreshold_1/blendMix/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/blendMix/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/blendMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXThreshold_1/blendReverse", [
			["bool_int", "/TXThreshold_1/blendReverse/fixedValue", 0],
			["bool_int", "/TXThreshold_1/blendReverse/fixedModMix", 0],
			["bool_int", "/TXThreshold_1/blendReverse/useExtMod", 0],
			["float", "/TXThreshold_1/blendReverse/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/drawAlpha", [
			["float", "/TXThreshold_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/drawAlpha/useExtMod", 0],
			["float", "/TXThreshold_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/drawPosX", [
			["float", "/TXThreshold_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXThreshold_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/drawPosX/useExtMod", 0],
			["float", "/TXThreshold_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/drawPosY", [
			["float", "/TXThreshold_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXThreshold_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/drawPosY/useExtMod", 0],
			["float", "/TXThreshold_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/drawWidth", [
			["float", "/TXThreshold_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/drawWidth/useExtMod", 0],
			["float", "/TXThreshold_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/drawHeight", [
			["float", "/TXThreshold_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXThreshold_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/drawHeight/useExtMod", 0],
			["float", "/TXThreshold_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/anchorX", [
			["float", "/TXThreshold_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXThreshold_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/anchorX/useExtMod", 0],
			["float", "/TXThreshold_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/anchorY", [
			["float", "/TXThreshold_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXThreshold_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/anchorY/useExtMod", 0],
			["float", "/TXThreshold_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXThreshold_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/rotate", [
			["float", "/TXThreshold_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXThreshold_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/rotate/useExtMod", 0],
			["float", "/TXThreshold_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/rotate/softMin", -360, -360, 360],
			["float", "/TXThreshold_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/rotateMultiply", [
			["float", "/TXThreshold_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXThreshold_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/rotateMultiply/useExtMod", 0],
			["float", "/TXThreshold_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXThreshold_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/scaleX", [
			["float", "/TXThreshold_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXThreshold_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/scaleX/useExtMod", 0],
			["float", "/TXThreshold_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXThreshold_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXThreshold_1/scaleY", [
			["float", "/TXThreshold_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXThreshold_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXThreshold_1/scaleY/useExtMod", 0],
			["float", "/TXThreshold_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXThreshold_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXThreshold_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXThreshold_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXThreshold_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Colors$Below Color", {arg parameterName;
				var arrNames = ["belowColorHue", "belowColorSaturation", "belowColorBrightness", "belowColorAlpha",
					"useSampledBelowColor", "swapColors", "belowSamplePosX", "belowSamplePosY", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Colors$Above Color", {arg parameterName;
				var arrNames = ["aboveColorHue", "aboveColorSaturation", "aboveColorBrightness", "aboveColorAlpha",
					"useSampledAboveColor", "aboveSamplePosX", "aboveSamplePosY"];
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
		^"Source Image, Threshold, Blend";
	}

}


