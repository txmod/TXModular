// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Glow : TXV_Module {
	classvar <defaultName = "TXV Glow";
	classvar <>arrInstances;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXGlow_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXGlow_1/drawActive", [
			["bool_int", "/TXGlow_1/drawActive/fixedValue", 1],
			["bool_int", "/TXGlow_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXGlow_1/drawActive/useExtMod", 0],
			["float", "/TXGlow_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXGlow_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXGlow_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXGlow_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXGlow_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXGlow_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXGlow_1/sourceImageScaleMode", [
			["int", "/TXGlow_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXGlow_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXGlow_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXGlow_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXGlow_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXGlow_1/renderContinuosly", [
			["bool_int", "/TXGlow_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXGlow_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXGlow_1/renderContinuosly/useExtMod", 0],
			["float", "/TXGlow_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGlow_1/renderNow", [
			["bool_int", "/TXGlow_1/renderNow/fixedValue", 0],
			["bool_int", "/TXGlow_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXGlow_1/renderNow/useExtMod", 0],
			["float", "/TXGlow_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXGlow_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXGlow_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXGlow_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXGlow_1/clearBeforeRender", [
			["bool_int", "/TXGlow_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXGlow_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXGlow_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXGlow_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGlow_1/clearNow", [
			["bool_int", "/TXGlow_1/clearNow/fixedValue", 0],
			["bool_int", "/TXGlow_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXGlow_1/clearNow/useExtMod", 0],
			["float", "/TXGlow_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/clearColorHue", [
			["float", "/TXGlow_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXGlow_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/clearColorHue/useExtMod", 0],
			["float", "/TXGlow_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXGlow_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/clearColorSaturation", [
			["float", "/TXGlow_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXGlow_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXGlow_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXGlow_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/clearColorBrightness", [
			["float", "/TXGlow_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXGlow_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXGlow_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXGlow_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/clearColorAlpha", [
			["float", "/TXGlow_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXGlow_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXGlow_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXGlow_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXGlow_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXGlow_1/glow", [
			["float", "/TXGlow_1/glow/fixedValue", 1, 0, 1],
			["float", "/TXGlow_1/glow/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/glow/useExtMod", 0],
			["float", "/TXGlow_1/glow/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/glow/softMin", 0, 0, 1],
			["float", "/TXGlow_1/glow/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/drawAlpha", [
			["float", "/TXGlow_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXGlow_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/drawAlpha/useExtMod", 0],
			["float", "/TXGlow_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXGlow_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/drawPosX", [
			["float", "/TXGlow_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXGlow_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/drawPosX/useExtMod", 0],
			["float", "/TXGlow_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXGlow_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/drawPosY", [
			["float", "/TXGlow_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXGlow_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/drawPosY/useExtMod", 0],
			["float", "/TXGlow_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXGlow_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/drawWidth", [
			["float", "/TXGlow_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXGlow_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/drawWidth/useExtMod", 0],
			["float", "/TXGlow_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXGlow_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/drawHeight", [
			["float", "/TXGlow_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXGlow_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/drawHeight/useExtMod", 0],
			["float", "/TXGlow_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXGlow_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/anchorX", [
			["float", "/TXGlow_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXGlow_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/anchorX/useExtMod", 0],
			["float", "/TXGlow_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXGlow_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/anchorY", [
			["float", "/TXGlow_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXGlow_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/anchorY/useExtMod", 0],
			["float", "/TXGlow_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXGlow_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/rotate", [
			["float", "/TXGlow_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXGlow_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/rotate/useExtMod", 0],
			["float", "/TXGlow_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/rotate/softMin", -360, -360, 360],
			["float", "/TXGlow_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/rotateMultiply", [
			["float", "/TXGlow_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXGlow_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/rotateMultiply/useExtMod", 0],
			["float", "/TXGlow_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXGlow_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/scaleX", [
			["float", "/TXGlow_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXGlow_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/scaleX/useExtMod", 0],
			["float", "/TXGlow_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXGlow_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXGlow_1/scaleY", [
			["float", "/TXGlow_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXGlow_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGlow_1/scaleY/useExtMod", 0],
			["float", "/TXGlow_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXGlow_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXGlow_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXGlow_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXGlow_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
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
		^"Source Image & Glow";
	}

}


