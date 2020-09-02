// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_SlideShow : TXV_Module {
	classvar <defaultName = "TXV Slide Show";
	classvar <>arrInstances;

	//NOTE: COMMENT OUT THE FOLLOWING PARAMETERS
	// ["assetSlot/image", "assetSlot/image/sourceImage2Asset", 0],
	// ["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
	// ["extImageModuleSlot", "extImageModuleSlot/extSourceImage2Module", 0],
	// ["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
	// ["bool_int", "/TXSlideShow_1/useExternalSourceImage", 0],
	// ["bool_int", "/TXSlideShow_1/useExternalSourceImage2", 0],
	// [ "modParameterGroupInt", "/TXSlideShow_1/sourceImage2ScaleMode", [
	// 	["int", "/TXSlideShow_1/sourceImage2ScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
	// 	["float", "/TXSlideShow_1/sourceImage2ScaleMode/fixedModMix", 0, 0, 1],
	// 	["bool_int", "/TXSlideShow_1/sourceImage2ScaleMode/useExtMod", 0],
	// 	["float", "/TXSlideShow_1/sourceImage2ScaleMode/extModValue", 0, 0, 1],
	// 	["int", "/TXSlideShow_1/sourceImage2ScaleMode/softMin", 0, 0, 2],
	// 	["int", "/TXSlideShow_1/sourceImage2ScaleMode/softMax", 2, 0, 2],
	// ]],

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

// add text for "xFadeType/fixedValue"
//, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXSlideShow_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		// ["assetSlot/image", "assetSlot/image/sourceImage2Asset", 0],
		// ["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		// ["extImageModuleSlot", "extImageModuleSlot/extSourceImage2Module", 0],
		// ["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXSlideShow_1/drawActive", [
			["bool_int", "/TXSlideShow_1/drawActive/fixedValue", 1],
			["bool_int", "/TXSlideShow_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/drawActive/useExtMod", 0],
			["float", "/TXSlideShow_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXSlideShow_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXSlideShow_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXSlideShow_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXSlideShow_1/customMaxDepth", 1024, 1, 4096],
		// ["bool_int", "/TXSlideShow_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXSlideShow_1/sourceImageScaleMode", [
			["int", "/TXSlideShow_1/sourceImageScaleMode/fixedValue", 1, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXSlideShow_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXSlideShow_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXSlideShow_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXSlideShow_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		// ["bool_int", "/TXSlideShow_1/useExternalSourceImage2", 0],
		// [ "modParameterGroupInt", "/TXSlideShow_1/sourceImage2ScaleMode", [
		// 	["int", "/TXSlideShow_1/sourceImage2ScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
		// 	["float", "/TXSlideShow_1/sourceImage2ScaleMode/fixedModMix", 0, 0, 1],
		// 	["bool_int", "/TXSlideShow_1/sourceImage2ScaleMode/useExtMod", 0],
		// 	["float", "/TXSlideShow_1/sourceImage2ScaleMode/extModValue", 0, 0, 1],
		// 	["int", "/TXSlideShow_1/sourceImage2ScaleMode/softMin", 0, 0, 2],
		// 	["int", "/TXSlideShow_1/sourceImage2ScaleMode/softMax", 2, 0, 2],
		// ]],
		[ "modParameterGroupBool", "/TXSlideShow_1/renderContinuosly", [
			["bool_int", "/TXSlideShow_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXSlideShow_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/renderContinuosly/useExtMod", 0],
			["float", "/TXSlideShow_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/renderNow", [
			["bool_int", "/TXSlideShow_1/renderNow/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/renderNow/useExtMod", 0],
			["float", "/TXSlideShow_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXSlideShow_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXSlideShow_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXSlideShow_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXSlideShow_1/clearBeforeRender", [
			["bool_int", "/TXSlideShow_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXSlideShow_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXSlideShow_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/clearNow", [
			["bool_int", "/TXSlideShow_1/clearNow/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/clearNow/useExtMod", 0],
			["float", "/TXSlideShow_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/clearColorHue", [
			["float", "/TXSlideShow_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXSlideShow_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/clearColorHue/useExtMod", 0],
			["float", "/TXSlideShow_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/clearColorSaturation", [
			["float", "/TXSlideShow_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXSlideShow_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXSlideShow_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/clearColorBrightness", [
			["float", "/TXSlideShow_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXSlideShow_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/clearColorAlpha", [
			["float", "/TXSlideShow_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXSlideShow_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXSlideShow_1/clearColorHSBAsRGB", 0],
		["string", "/TXSlideShow_1/folderName", ""],
		[ "modParameterGroupBool", "/TXSlideShow_1/randomiseOrder", [
			["bool_int", "/TXSlideShow_1/randomiseOrder/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/randomiseOrder/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/randomiseOrder/useExtMod", 0],
			["float", "/TXSlideShow_1/randomiseOrder/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXSlideShow_1/xFadeType", [
			["int", "/TXSlideShow_1/xFadeType/fixedValue", 23, 0, 23, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXSlideShow_1/xFadeType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/xFadeType/useExtMod", 0],
			["float", "/TXSlideShow_1/xFadeType/extModValue", 0, 0, 1],
			["int", "/TXSlideShow_1/xFadeType/softMin", 0, 0, 23],
			["int", "/TXSlideShow_1/xFadeType/softMax", 23, 0, 23],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/xFadeTimeFixed", [
			["float", "/TXSlideShow_1/xFadeTimeFixed/fixedValue", 3, 0.1, 10],
			["float", "/TXSlideShow_1/xFadeTimeFixed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/xFadeTimeFixed/useExtMod", 0],
			["float", "/TXSlideShow_1/xFadeTimeFixed/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/xFadeTimeFixed/softMin", 0.1, 0.1, 1000],
			["float", "/TXSlideShow_1/xFadeTimeFixed/softMax", 10, 0.1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/xFadeTimeRandom", [
			["float", "/TXSlideShow_1/xFadeTimeRandom/fixedValue", 0, 0, 10],
			["float", "/TXSlideShow_1/xFadeTimeRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/xFadeTimeRandom/useExtMod", 0],
			["float", "/TXSlideShow_1/xFadeTimeRandom/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/xFadeTimeRandom/softMin", 0, 0, 1000],
			["float", "/TXSlideShow_1/xFadeTimeRandom/softMax", 10, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/holdTimeFixed", [
			["float", "/TXSlideShow_1/holdTimeFixed/fixedValue", 3, 0.1, 10],
			["float", "/TXSlideShow_1/holdTimeFixed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/holdTimeFixed/useExtMod", 0],
			["float", "/TXSlideShow_1/holdTimeFixed/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/holdTimeFixed/softMin", 0.1, 0.1, 1000],
			["float", "/TXSlideShow_1/holdTimeFixed/softMax", 10, 0.1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/holdTimeRandom", [
			["float", "/TXSlideShow_1/holdTimeRandom/fixedValue", 0, 0, 10],
			["float", "/TXSlideShow_1/holdTimeRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/holdTimeRandom/useExtMod", 0],
			["float", "/TXSlideShow_1/holdTimeRandom/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/holdTimeRandom/softMin", 0, 0, 1000],
			["float", "/TXSlideShow_1/holdTimeRandom/softMax", 10, 0, 1000],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/runSlideShow", [
			["bool_int", "/TXSlideShow_1/runSlideShow/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/runSlideShow/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/runSlideShow/useExtMod", 0],
			["float", "/TXSlideShow_1/runSlideShow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/pauseSlideShow", [
			["bool_int", "/TXSlideShow_1/pauseSlideShow/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/pauseSlideShow/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/pauseSlideShow/useExtMod", 0],
			["float", "/TXSlideShow_1/pauseSlideShow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/triggerXFadeForwards", [
			["bool_int", "/TXSlideShow_1/triggerXFadeForwards/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/triggerXFadeForwards/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/triggerXFadeForwards/useExtMod", 0],
			["float", "/TXSlideShow_1/triggerXFadeForwards/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/triggerXFadeBackwards", [
			["bool_int", "/TXSlideShow_1/triggerXFadeBackwards/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/triggerXFadeBackwards/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/triggerXFadeBackwards/useExtMod", 0],
			["float", "/TXSlideShow_1/triggerXFadeBackwards/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/triggerXFadeToRandom", [
			["bool_int", "/TXSlideShow_1/triggerXFadeToRandom/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/triggerXFadeToRandom/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/triggerXFadeToRandom/useExtMod", 0],
			["float", "/TXSlideShow_1/triggerXFadeToRandom/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/jumpToFirstImage", [
			["bool_int", "/TXSlideShow_1/jumpToFirstImage/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/jumpToFirstImage/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/jumpToFirstImage/useExtMod", 0],
			["float", "/TXSlideShow_1/jumpToFirstImage/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/jumpToNextImage", [
			["bool_int", "/TXSlideShow_1/jumpToNextImage/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/jumpToNextImage/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/jumpToNextImage/useExtMod", 0],
			["float", "/TXSlideShow_1/jumpToNextImage/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/jumpToPreviousImage", [
			["bool_int", "/TXSlideShow_1/jumpToPreviousImage/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/jumpToPreviousImage/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/jumpToPreviousImage/useExtMod", 0],
			["float", "/TXSlideShow_1/jumpToPreviousImage/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSlideShow_1/jumpToRandomImage", [
			["bool_int", "/TXSlideShow_1/jumpToRandomImage/fixedValue", 0],
			["bool_int", "/TXSlideShow_1/jumpToRandomImage/fixedModMix", 0],
			["bool_int", "/TXSlideShow_1/jumpToRandomImage/useExtMod", 0],
			["float", "/TXSlideShow_1/jumpToRandomImage/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/drawAlpha", [
			["float", "/TXSlideShow_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXSlideShow_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/drawAlpha/useExtMod", 0],
			["float", "/TXSlideShow_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/drawPosX", [
			["float", "/TXSlideShow_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXSlideShow_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/drawPosX/useExtMod", 0],
			["float", "/TXSlideShow_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/drawPosY", [
			["float", "/TXSlideShow_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXSlideShow_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/drawPosY/useExtMod", 0],
			["float", "/TXSlideShow_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/drawWidth", [
			["float", "/TXSlideShow_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXSlideShow_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/drawWidth/useExtMod", 0],
			["float", "/TXSlideShow_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/drawHeight", [
			["float", "/TXSlideShow_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXSlideShow_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/drawHeight/useExtMod", 0],
			["float", "/TXSlideShow_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/anchorX", [
			["float", "/TXSlideShow_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXSlideShow_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/anchorX/useExtMod", 0],
			["float", "/TXSlideShow_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/anchorY", [
			["float", "/TXSlideShow_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXSlideShow_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/anchorY/useExtMod", 0],
			["float", "/TXSlideShow_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXSlideShow_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/rotate", [
			["float", "/TXSlideShow_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXSlideShow_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/rotate/useExtMod", 0],
			["float", "/TXSlideShow_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/rotate/softMin", -360, -360, 360],
			["float", "/TXSlideShow_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/rotateMultiply", [
			["float", "/TXSlideShow_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXSlideShow_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/rotateMultiply/useExtMod", 0],
			["float", "/TXSlideShow_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXSlideShow_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/scaleX", [
			["float", "/TXSlideShow_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXSlideShow_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/scaleX/useExtMod", 0],
			["float", "/TXSlideShow_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXSlideShow_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXSlideShow_1/scaleY", [
			["float", "/TXSlideShow_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXSlideShow_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSlideShow_1/scaleY/useExtMod", 0],
			["float", "/TXSlideShow_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXSlideShow_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXSlideShow_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXSlideShow_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXSlideShow_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Times", {arg parameterName;
				parameterName.containsi("time")
				;}],
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Draw$Active, Size, Alpha",
				{arg parameterName;
					var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
						"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
						"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
						"drawAlpha", "drawWidth", "drawHeight", ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale",
				{arg parameterName;
					var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate",
				{arg parameterName;
					var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY",
						"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {
		^"Source & X-fade";
	}

}
