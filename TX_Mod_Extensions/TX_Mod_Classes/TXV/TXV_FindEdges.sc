// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_FindEdges : TXV_Module {
	classvar <defaultName = "TXV FindEdges";
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
		/* ---------- TXV Settable Parameter list for: TXFindEdges_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXFindEdges_1/drawActive", [
			["bool_int", "/TXFindEdges_1/drawActive/fixedValue", 1],
			["bool_int", "/TXFindEdges_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/drawActive/useExtMod", 0],
			["float", "/TXFindEdges_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXFindEdges_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXFindEdges_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXFindEdges_1/customMaxHeight", 768, 1, 4096],
		["bool_int", "/TXFindEdges_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXFindEdges_1/sourceImageScaleMode", [
			["int", "/TXFindEdges_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXFindEdges_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXFindEdges_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXFindEdges_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXFindEdges_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXFindEdges_1/renderContinuosly", [
			["bool_int", "/TXFindEdges_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXFindEdges_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/renderContinuosly/useExtMod", 0],
			["float", "/TXFindEdges_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFindEdges_1/renderNow", [
			["bool_int", "/TXFindEdges_1/renderNow/fixedValue", 0],
			["bool_int", "/TXFindEdges_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/renderNow/useExtMod", 0],
			["float", "/TXFindEdges_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXFindEdges_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXFindEdges_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXFindEdges_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXFindEdges_1/clearBeforeRender", [
			["bool_int", "/TXFindEdges_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXFindEdges_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXFindEdges_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFindEdges_1/clearNow", [
			["bool_int", "/TXFindEdges_1/clearNow/fixedValue", 0],
			["bool_int", "/TXFindEdges_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/clearNow/useExtMod", 0],
			["float", "/TXFindEdges_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/clearColorHue", [
			["float", "/TXFindEdges_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXFindEdges_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/clearColorHue/useExtMod", 0],
			["float", "/TXFindEdges_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/clearColorSaturation", [
			["float", "/TXFindEdges_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXFindEdges_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXFindEdges_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/clearColorBrightness", [
			["float", "/TXFindEdges_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXFindEdges_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/clearColorAlpha", [
			["float", "/TXFindEdges_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXFindEdges_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXFindEdges_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupBool", "/TXFindEdges_1/showEdgeColor", [
			["bool_int", "/TXFindEdges_1/showEdgeColor/fixedValue", 1],
			["bool_int", "/TXFindEdges_1/showEdgeColor/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/showEdgeColor/useExtMod", 0],
			["float", "/TXFindEdges_1/showEdgeColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXFindEdges_1/blendMode", [
			["int", "/TXFindEdges_1/blendMode/fixedValue", 0, 0, 26, ["Mix (default)", "Lighten", "Darken", "Multiply", "Average", "Add", "Subtract", "Difference", "Negation", "Exclusion", "Screen", "Overlay", "SoftLight", "HardLight", "ColorDodge", "ColorBurn", "LinearLight", "VividLight", "PinLight", "HardMix", "Reflect", "Glow", "Phoenix", "BlendHue", "BlendSaturation", "BlendColor", "BlendLuminosity",]],
			["float", "/TXFindEdges_1/blendMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/blendMode/useExtMod", 0],
			["float", "/TXFindEdges_1/blendMode/extModValue", 0, 0, 1],
			["int", "/TXFindEdges_1/blendMode/softMin", 0, 0, 26],
			["int", "/TXFindEdges_1/blendMode/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/blendMix", [
			["float", "/TXFindEdges_1/blendMix/fixedValue", 1, 0, 1],
			["float", "/TXFindEdges_1/blendMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/blendMix/useExtMod", 0],
			["float", "/TXFindEdges_1/blendMix/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/blendMix/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/blendMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXFindEdges_1/blendReverse", [
			["bool_int", "/TXFindEdges_1/blendReverse/fixedValue", 0],
			["bool_int", "/TXFindEdges_1/blendReverse/fixedModMix", 0],
			["bool_int", "/TXFindEdges_1/blendReverse/useExtMod", 0],
			["float", "/TXFindEdges_1/blendReverse/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/drawAlpha", [
			["float", "/TXFindEdges_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXFindEdges_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/drawAlpha/useExtMod", 0],
			["float", "/TXFindEdges_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/drawPosX", [
			["float", "/TXFindEdges_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXFindEdges_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/drawPosX/useExtMod", 0],
			["float", "/TXFindEdges_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/drawPosY", [
			["float", "/TXFindEdges_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXFindEdges_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/drawPosY/useExtMod", 0],
			["float", "/TXFindEdges_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/drawWidth", [
			["float", "/TXFindEdges_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXFindEdges_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/drawWidth/useExtMod", 0],
			["float", "/TXFindEdges_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/drawHeight", [
			["float", "/TXFindEdges_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXFindEdges_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/drawHeight/useExtMod", 0],
			["float", "/TXFindEdges_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/anchorX", [
			["float", "/TXFindEdges_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXFindEdges_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/anchorX/useExtMod", 0],
			["float", "/TXFindEdges_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/anchorY", [
			["float", "/TXFindEdges_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXFindEdges_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/anchorY/useExtMod", 0],
			["float", "/TXFindEdges_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXFindEdges_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/rotate", [
			["float", "/TXFindEdges_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXFindEdges_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/rotate/useExtMod", 0],
			["float", "/TXFindEdges_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/rotate/softMin", -360, -360, 360],
			["float", "/TXFindEdges_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/rotateMultiply", [
			["float", "/TXFindEdges_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXFindEdges_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/rotateMultiply/useExtMod", 0],
			["float", "/TXFindEdges_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXFindEdges_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/scaleX", [
			["float", "/TXFindEdges_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXFindEdges_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/scaleX/useExtMod", 0],
			["float", "/TXFindEdges_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXFindEdges_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXFindEdges_1/scaleY", [
			["float", "/TXFindEdges_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXFindEdges_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXFindEdges_1/scaleY/useExtMod", 0],
			["float", "/TXFindEdges_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXFindEdges_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXFindEdges_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXFindEdges_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXFindEdges_1 ---------- */
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
		^"Image, Edge Color, Blend";
	}

}


