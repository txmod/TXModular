// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawTransform3D : TXV_Module {
	classvar <defaultName = "TXV Transform3D";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maxDepthRule":
	// , ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	// add text array to ... "transformOrder/fixedValue"
	// , ["Shift Rotate Scale", "Shift Scale Rotate", "Rotate Scale Shift", "Rotate Shift Scale", "Scale Shift Rotate", "Scale Rotate Shift"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawTransform3D_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawTransform3D_1/drawActive", [
			["bool_int", "/TXDrawTransform3D_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawTransform3D_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3D_1/drawActive/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawTransform3D_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawTransform3D_1/maxDepthRule", 0, 0, 4, ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]],
		["int", "/TXDrawTransform3D_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawTransform3D_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXDrawTransform3D_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawTransform3D_1/drawLayersRule", [
			["int", "/TXDrawTransform3D_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawTransform3D_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/drawLayersRule/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawTransform3D_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawTransform3D_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupInt", "/TXDrawTransform3D_1/transformOrder", [
			["int", "/TXDrawTransform3D_1/transformOrder/fixedValue", 0, 0, 5, ["Shift Rotate Scale", "Shift Scale Rotate", "Rotate Scale Shift", "Rotate Shift Scale", "Scale Shift Rotate", "Scale Rotate Shift"]],
			["float", "/TXDrawTransform3D_1/transformOrder/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/transformOrder/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/transformOrder/extModValue", 0, 0, 1],
			["int", "/TXDrawTransform3D_1/transformOrder/softMin", 0, 0, 5],
			["int", "/TXDrawTransform3D_1/transformOrder/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/shiftX", [
			["float", "/TXDrawTransform3D_1/shiftX/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3D_1/shiftX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/shiftX/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/shiftX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/shiftX/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3D_1/shiftX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/shiftY", [
			["float", "/TXDrawTransform3D_1/shiftY/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3D_1/shiftY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/shiftY/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/shiftY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/shiftY/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3D_1/shiftY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/shiftZ", [
			["float", "/TXDrawTransform3D_1/shiftZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3D_1/shiftZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/shiftZ/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/shiftZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/shiftZ/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3D_1/shiftZ/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/rotateX", [
			["float", "/TXDrawTransform3D_1/rotateX/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3D_1/rotateX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/rotateX/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/rotateX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/rotateX/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3D_1/rotateX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/rotateY", [
			["float", "/TXDrawTransform3D_1/rotateY/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3D_1/rotateY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/rotateY/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/rotateY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/rotateY/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3D_1/rotateY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/rotateZ", [
			["float", "/TXDrawTransform3D_1/rotateZ/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3D_1/rotateZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/rotateZ/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/rotateZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/rotateZ/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3D_1/rotateZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/rotateMultiply", [
			["float", "/TXDrawTransform3D_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawTransform3D_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawTransform3D_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/anchorX", [
			["float", "/TXDrawTransform3D_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform3D_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/anchorX/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/anchorX/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3D_1/anchorX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/anchorY", [
			["float", "/TXDrawTransform3D_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform3D_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/anchorY/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/anchorY/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3D_1/anchorY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/anchorZ", [
			["float", "/TXDrawTransform3D_1/anchorZ/fixedValue", 0, -1, 0],
			["float", "/TXDrawTransform3D_1/anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/anchorZ/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/anchorZ/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3D_1/anchorZ/softMax", 0, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/scaleX", [
			["float", "/TXDrawTransform3D_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3D_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/scaleX/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3D_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/scaleY", [
			["float", "/TXDrawTransform3D_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3D_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/scaleY/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3D_1/scaleY/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3D_1/scaleZ", [
			["float", "/TXDrawTransform3D_1/scaleZ/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3D_1/scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3D_1/scaleZ/useExtMod", 0],
			["float", "/TXDrawTransform3D_1/scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3D_1/scaleZ/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3D_1/scaleZ/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawTransform3D_1/useScaleXForScaleY", 0],
		["bool_int", "/TXDrawTransform3D_1/useScaleXForScaleZ", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawTransform3D_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Transform & Shift", {arg parameterName;
				parameterName.containsi("shift")
				|| parameterName.containsi("transformOrder")
				;}],
			["Scale", {arg parameterName;
					parameterName.containsi("scale")
				;}],
			["Rotate", {arg parameterName;
				parameterName.containsi("rotate")
				|| parameterName.containsi("anchor")
				;}],
			["Draw Parameters", {arg parameterName;
				var drawParameterNames = ["drawActive", "maxWidthHeightRule", "maxDepthRule",
					"customMaxWidth", "customMaxHeight", "customMaxDepth", "drawLayersRule",];
				// return validity bool
				drawParameterNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

		// override
	getInitialDisplayOption {
		^"Transform & Shift";
	}

}


