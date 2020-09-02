// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Draw3DModel : TXV_Module {
	classvar <defaultName = "TXV 3D Model";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."loopType/fixedValue":
	// , ["No Looping", "Forwards & Backwards Looping", "Normal Looping", ]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDraw3DModel_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/3DModel", "assetSlot/3DModel/modelAsset", 0],
		[ "modParameterGroupBool", "/TXDraw3DModel_1/drawActive", [
			["bool_int", "/TXDraw3DModel_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDraw3DModel_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDraw3DModel_1/drawActive/useExtMod", 0],
			["float", "/TXDraw3DModel_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDraw3DModel_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDraw3DModel_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDraw3DModel_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXDraw3DModel_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupBool", "/TXDraw3DModel_1/createLightsFromModel", [
			["bool_int", "/TXDraw3DModel_1/createLightsFromModel/fixedValue", 0],
			["bool_int", "/TXDraw3DModel_1/createLightsFromModel/fixedModMix", 0],
			["bool_int", "/TXDraw3DModel_1/createLightsFromModel/useExtMod", 0],
			["float", "/TXDraw3DModel_1/createLightsFromModel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDraw3DModel_1/loopType", [
			["int", "/TXDraw3DModel_1/loopType/fixedValue", 3, 1, 3, ["No Looping", "Forwards & Backwards Looping", "Normal Looping", ]],
			["float", "/TXDraw3DModel_1/loopType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/loopType/useExtMod", 0],
			["float", "/TXDraw3DModel_1/loopType/extModValue", 0, 0, 1],
			["int", "/TXDraw3DModel_1/loopType/softMin", 1, 1, 3],
			["int", "/TXDraw3DModel_1/loopType/softMax", 3, 1, 3],
		]],
		[ "modParameterGroupBool", "/TXDraw3DModel_1/play", [
			["bool_int", "/TXDraw3DModel_1/play/fixedValue", 0],
			["bool_int", "/TXDraw3DModel_1/play/fixedModMix", 0],
			["bool_int", "/TXDraw3DModel_1/play/useExtMod", 0],
			["float", "/TXDraw3DModel_1/play/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw3DModel_1/pause", [
			["bool_int", "/TXDraw3DModel_1/pause/fixedValue", 0],
			["bool_int", "/TXDraw3DModel_1/pause/fixedModMix", 0],
			["bool_int", "/TXDraw3DModel_1/pause/useExtMod", 0],
			["float", "/TXDraw3DModel_1/pause/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw3DModel_1/goToStart", [
			["bool_int", "/TXDraw3DModel_1/goToStart/fixedValue", 0],
			["bool_int", "/TXDraw3DModel_1/goToStart/fixedModMix", 0],
			["bool_int", "/TXDraw3DModel_1/goToStart/useExtMod", 0],
			["float", "/TXDraw3DModel_1/goToStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/animationPosition", [
			["float", "/TXDraw3DModel_1/animationPosition/fixedValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/animationPosition/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/animationPosition/useExtMod", 0],
			["float", "/TXDraw3DModel_1/animationPosition/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/animationPosition/softMin", 0, 0, 1],
			["float", "/TXDraw3DModel_1/animationPosition/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/drawPosX", [
			["float", "/TXDraw3DModel_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw3DModel_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/drawPosX/useExtMod", 0],
			["float", "/TXDraw3DModel_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/drawPosX/softMin", 0, -20, 20],
			["float", "/TXDraw3DModel_1/drawPosX/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/drawPosY", [
			["float", "/TXDraw3DModel_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw3DModel_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/drawPosY/useExtMod", 0],
			["float", "/TXDraw3DModel_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/drawPosY/softMin", 0, -20, 20],
			["float", "/TXDraw3DModel_1/drawPosY/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/drawPosZ", [
			["float", "/TXDraw3DModel_1/drawPosZ/fixedValue", 0, -1, 1],
			["float", "/TXDraw3DModel_1/drawPosZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/drawPosZ/useExtMod", 0],
			["float", "/TXDraw3DModel_1/drawPosZ/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/drawPosZ/softMin", -1, -20, 20],
			["float", "/TXDraw3DModel_1/drawPosZ/softMax", 1, -20, 20],
		]],
		["bool_int", "/TXDraw3DModel_1/adjustPosToSceneCentre", 0],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/rotateX", [
			["float", "/TXDraw3DModel_1/rotateX/fixedValue", 0, -360, 360],
			["float", "/TXDraw3DModel_1/rotateX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/rotateX/useExtMod", 0],
			["float", "/TXDraw3DModel_1/rotateX/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/rotateX/softMin", -360, -360, 360],
			["float", "/TXDraw3DModel_1/rotateX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/rotateY", [
			["float", "/TXDraw3DModel_1/rotateY/fixedValue", 0, -360, 360],
			["float", "/TXDraw3DModel_1/rotateY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/rotateY/useExtMod", 0],
			["float", "/TXDraw3DModel_1/rotateY/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/rotateY/softMin", -360, -360, 360],
			["float", "/TXDraw3DModel_1/rotateY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/rotateZ", [
			["float", "/TXDraw3DModel_1/rotateZ/fixedValue", 0, -360, 360],
			["float", "/TXDraw3DModel_1/rotateZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/rotateZ/useExtMod", 0],
			["float", "/TXDraw3DModel_1/rotateZ/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/rotateZ/softMin", -360, -360, 360],
			["float", "/TXDraw3DModel_1/rotateZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/rotateMultiply", [
			["float", "/TXDraw3DModel_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDraw3DModel_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDraw3DModel_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDraw3DModel_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/scaleX", [
			["float", "/TXDraw3DModel_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDraw3DModel_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/scaleX/useExtMod", 0],
			["float", "/TXDraw3DModel_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDraw3DModel_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/scaleY", [
			["float", "/TXDraw3DModel_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDraw3DModel_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/scaleY/useExtMod", 0],
			["float", "/TXDraw3DModel_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDraw3DModel_1/scaleY/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw3DModel_1/scaleZ", [
			["float", "/TXDraw3DModel_1/scaleZ/fixedValue", 1, 0, 3],
			["float", "/TXDraw3DModel_1/scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw3DModel_1/scaleZ/useExtMod", 0],
			["float", "/TXDraw3DModel_1/scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDraw3DModel_1/scaleZ/softMin", 0, -10, 10],
			["float", "/TXDraw3DModel_1/scaleZ/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDraw3DModel_1/useScaleXForScaleY", 0],
		["bool_int", "/TXDraw3DModel_1/useScaleXForScaleZ", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDraw3DModel_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Position", {arg parameterName;
				parameterName.containsi("drawPos")
				|| parameterName.containsi("adjustPosToSceneCentre")
				;}],
			["Scale", {arg parameterName;
				parameterName.containsi("scale")
				;}],
			["Rotate", {arg parameterName;
				parameterName.containsi("rotate")
				;}],
			["Draw Parameters", {arg parameterName;
				var drawParameterNames = ["drawActive", "maxWidthHeightRule", "customMaxWidth", "customMaxHeight", "customMaxDepth", ];
				// return validity bool
				drawParameterNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

}


