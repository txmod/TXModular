// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Blur : TXV_Module {
	classvar <defaultName = "TXV Blur";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXBlur_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXBlur_1/drawActive", [
			["bool_int", "/TXBlur_1/drawActive/fixedValue", 1],
			["bool_int", "/TXBlur_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXBlur_1/drawActive/useExtMod", 0],
			["float", "/TXBlur_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXBlur_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXBlur_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXBlur_1/customMaxHeight", 768, 1, 4096],
		["bool_int", "/TXBlur_1/useExternalSourceImage", 0],
		[ "modParameterGroupBool", "/TXBlur_1/renderContinuosly", [
			["bool_int", "/TXBlur_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXBlur_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXBlur_1/renderContinuosly/useExtMod", 0],
			["float", "/TXBlur_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXBlur_1/renderNow", [
			["bool_int", "/TXBlur_1/renderNow/fixedValue", 0],
			["bool_int", "/TXBlur_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXBlur_1/renderNow/useExtMod", 0],
			["float", "/TXBlur_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXBlur_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXBlur_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXBlur_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupFloat", "/TXBlur_1/blurDistance", [
			["float", "/TXBlur_1/blurDistance/fixedValue", 1.1, 0, 10],
			["float", "/TXBlur_1/blurDistance/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/blurDistance/useExtMod", 0],
			["float", "/TXBlur_1/blurDistance/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/blurDistance/softMin", 0, 0, 100],
			["float", "/TXBlur_1/blurDistance/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXBlur_1/noPasses", [
			["int", "/TXBlur_1/noPasses/fixedValue", 2, 0, 5],
			["float", "/TXBlur_1/noPasses/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/noPasses/useExtMod", 0],
			["float", "/TXBlur_1/noPasses/extModValue", 0, 0, 1],
			["int", "/TXBlur_1/noPasses/softMin", 0, 0, 20],
			["int", "/TXBlur_1/noPasses/softMax", 5, 0, 20],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/drawAlpha", [
			["float", "/TXBlur_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXBlur_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/drawAlpha/useExtMod", 0],
			["float", "/TXBlur_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXBlur_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/drawPosX", [
			["float", "/TXBlur_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXBlur_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/drawPosX/useExtMod", 0],
			["float", "/TXBlur_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXBlur_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/drawPosY", [
			["float", "/TXBlur_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXBlur_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/drawPosY/useExtMod", 0],
			["float", "/TXBlur_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXBlur_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/drawWidth", [
			["float", "/TXBlur_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXBlur_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/drawWidth/useExtMod", 0],
			["float", "/TXBlur_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXBlur_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/drawHeight", [
			["float", "/TXBlur_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXBlur_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/drawHeight/useExtMod", 0],
			["float", "/TXBlur_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXBlur_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/anchorX", [
			["float", "/TXBlur_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXBlur_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/anchorX/useExtMod", 0],
			["float", "/TXBlur_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXBlur_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/anchorY", [
			["float", "/TXBlur_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXBlur_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/anchorY/useExtMod", 0],
			["float", "/TXBlur_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXBlur_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/rotate", [
			["float", "/TXBlur_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXBlur_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/rotate/useExtMod", 0],
			["float", "/TXBlur_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/rotate/softMin", -360, -360, 360],
			["float", "/TXBlur_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/rotateMultiply", [
			["float", "/TXBlur_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXBlur_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/rotateMultiply/useExtMod", 0],
			["float", "/TXBlur_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXBlur_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/scaleX", [
			["float", "/TXBlur_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXBlur_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/scaleX/useExtMod", 0],
			["float", "/TXBlur_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXBlur_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXBlur_1/scaleY", [
			["float", "/TXBlur_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXBlur_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXBlur_1/scaleY/useExtMod", 0],
			["float", "/TXBlur_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXBlur_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXBlur_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXBlur_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXBlur_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Render", {arg parameterName;
				parameterName.containsi("render")
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
		^"Image & Blur";
	}


}


