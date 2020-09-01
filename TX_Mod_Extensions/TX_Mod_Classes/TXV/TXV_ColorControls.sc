// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_ColorControls : TXV_Module {
	classvar <defaultName = "TXV Color Controls";
	classvar <>arrInstances;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXColourControls_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXColourControls_1/drawActive", [
			["bool_int", "/TXColourControls_1/drawActive/fixedValue", 1],
			["bool_int", "/TXColourControls_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/drawActive/useExtMod", 0],
			["float", "/TXColourControls_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXColourControls_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXColourControls_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXColourControls_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXColourControls_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXColourControls_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXColourControls_1/sourceImageScaleMode", [
			["int", "/TXColourControls_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXColourControls_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXColourControls_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXColourControls_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXColourControls_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXColourControls_1/renderContinuosly", [
			["bool_int", "/TXColourControls_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXColourControls_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/renderContinuosly/useExtMod", 0],
			["float", "/TXColourControls_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColourControls_1/renderNow", [
			["bool_int", "/TXColourControls_1/renderNow/fixedValue", 0],
			["bool_int", "/TXColourControls_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/renderNow/useExtMod", 0],
			["float", "/TXColourControls_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXColourControls_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXColourControls_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXColourControls_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXColourControls_1/clearBeforeRender", [
			["bool_int", "/TXColourControls_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXColourControls_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXColourControls_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColourControls_1/clearNow", [
			["bool_int", "/TXColourControls_1/clearNow/fixedValue", 0],
			["bool_int", "/TXColourControls_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/clearNow/useExtMod", 0],
			["float", "/TXColourControls_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/clearColorHue", [
			["float", "/TXColourControls_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/clearColorHue/useExtMod", 0],
			["float", "/TXColourControls_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/clearColorSaturation", [
			["float", "/TXColourControls_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXColourControls_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXColourControls_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/clearColorBrightness", [
			["float", "/TXColourControls_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXColourControls_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/clearColorAlpha", [
			["float", "/TXColourControls_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXColourControls_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXColourControls_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXColourControls_1/brightness", [
			["float", "/TXColourControls_1/brightness/fixedValue", 1, 0, 2],
			["float", "/TXColourControls_1/brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/brightness/useExtMod", 0],
			["float", "/TXColourControls_1/brightness/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/brightness/softMin", 0, 0, 4],
			["float", "/TXColourControls_1/brightness/softMax", 2, 0, 4],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/contrast", [
			["float", "/TXColourControls_1/contrast/fixedValue", 1, 0.25, 2],
			["float", "/TXColourControls_1/contrast/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/contrast/useExtMod", 0],
			["float", "/TXColourControls_1/contrast/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/contrast/softMin", 0.25, 0, 4],
			["float", "/TXColourControls_1/contrast/softMax", 2, 0, 4],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/contrastMidPoint", [
			["float", "/TXColourControls_1/contrastMidPoint/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/contrastMidPoint/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/contrastMidPoint/useExtMod", 0],
			["float", "/TXColourControls_1/contrastMidPoint/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/contrastMidPoint/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/contrastMidPoint/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/saturation", [
			["float", "/TXColourControls_1/saturation/fixedValue", 1, 0, 2],
			["float", "/TXColourControls_1/saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/saturation/useExtMod", 0],
			["float", "/TXColourControls_1/saturation/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/saturation/softMin", 0, 0, 4],
			["float", "/TXColourControls_1/saturation/softMax", 2, 0, 4],
		]],
		[ "modParameterGroupBool", "/TXColourControls_1/useHueshift", [
			["bool_int", "/TXColourControls_1/useHueshift/fixedValue", 0],
			["bool_int", "/TXColourControls_1/useHueshift/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/useHueshift/useExtMod", 0],
			["float", "/TXColourControls_1/useHueshift/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/hueshift", [
			["float", "/TXColourControls_1/hueshift/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/hueshift/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/hueshift/useExtMod", 0],
			["float", "/TXColourControls_1/hueshift/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/hueshift/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/hueshift/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXColourControls_1/invert", [
			["bool_int", "/TXColourControls_1/invert/fixedValue", 0],
			["bool_int", "/TXColourControls_1/invert/fixedModMix", 0],
			["bool_int", "/TXColourControls_1/invert/useExtMod", 0],
			["float", "/TXColourControls_1/invert/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/fxMix", [
			["float", "/TXColourControls_1/fxMix/fixedValue", 1, 0, 1],
			["float", "/TXColourControls_1/fxMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/fxMix/useExtMod", 0],
			["float", "/TXColourControls_1/fxMix/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/fxMix/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/fxMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/drawAlpha", [
			["float", "/TXColourControls_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXColourControls_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/drawAlpha/useExtMod", 0],
			["float", "/TXColourControls_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/drawPosX", [
			["float", "/TXColourControls_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/drawPosX/useExtMod", 0],
			["float", "/TXColourControls_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/drawPosY", [
			["float", "/TXColourControls_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/drawPosY/useExtMod", 0],
			["float", "/TXColourControls_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/drawWidth", [
			["float", "/TXColourControls_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXColourControls_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/drawWidth/useExtMod", 0],
			["float", "/TXColourControls_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/drawHeight", [
			["float", "/TXColourControls_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXColourControls_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/drawHeight/useExtMod", 0],
			["float", "/TXColourControls_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/anchorX", [
			["float", "/TXColourControls_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/anchorX/useExtMod", 0],
			["float", "/TXColourControls_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/anchorY", [
			["float", "/TXColourControls_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXColourControls_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/anchorY/useExtMod", 0],
			["float", "/TXColourControls_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXColourControls_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/rotate", [
			["float", "/TXColourControls_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXColourControls_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/rotate/useExtMod", 0],
			["float", "/TXColourControls_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/rotate/softMin", -360, -360, 360],
			["float", "/TXColourControls_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/rotateMultiply", [
			["float", "/TXColourControls_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXColourControls_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/rotateMultiply/useExtMod", 0],
			["float", "/TXColourControls_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXColourControls_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/scaleX", [
			["float", "/TXColourControls_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXColourControls_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/scaleX/useExtMod", 0],
			["float", "/TXColourControls_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXColourControls_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXColourControls_1/scaleY", [
			["float", "/TXColourControls_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXColourControls_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColourControls_1/scaleY/useExtMod", 0],
			["float", "/TXColourControls_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXColourControls_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXColourControls_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXColourControls_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXColourControls_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Color Adjust", {arg parameterName;
				var arrNames = ["brightness", "contrast", "contrastMidPoint", "saturation", "useHueshift", "hueshift", "invert", "fxMix", ];
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
		^"Image";
	}

}

TXV_ColourControls : TXV_ColorControls {}

