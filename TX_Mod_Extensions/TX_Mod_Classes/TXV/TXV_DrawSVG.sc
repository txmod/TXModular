// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawSVG : TXV_Module {
	classvar <defaultName = "TXV SVG";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maskType/fixedValue":
	// , ["no mask", "use mask image", "feathered square", "circle", "feathered circle", "blurred circle", "ring", "feathered ring", "diamond", "feathered diamond",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawSVG_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/svg", "assetSlot/svg/svgAsset", 0],
		[ "modParameterGroupBool", "/TXDrawSVG_1/drawActive", [
			["bool_int", "/TXDrawSVG_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawSVG_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawSVG_1/drawActive/useExtMod", 0],
			["float", "/TXDrawSVG_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawSVG_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawSVG_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawSVG_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/positionX", [
			["float", "/TXDrawSVG_1/positionX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawSVG_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/positionX/useExtMod", 0],
			["float", "/TXDrawSVG_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/positionX/softMin", 0, 0, 1],
			["float", "/TXDrawSVG_1/positionX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/positionY", [
			["float", "/TXDrawSVG_1/positionY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawSVG_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/positionY/useExtMod", 0],
			["float", "/TXDrawSVG_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/positionY/softMin", 0, 0, 1],
			["float", "/TXDrawSVG_1/positionY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/anchorX", [
			["float", "/TXDrawSVG_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawSVG_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/anchorX/useExtMod", 0],
			["float", "/TXDrawSVG_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawSVG_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/anchorY", [
			["float", "/TXDrawSVG_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawSVG_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/anchorY/useExtMod", 0],
			["float", "/TXDrawSVG_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawSVG_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/rotate", [
			["float", "/TXDrawSVG_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawSVG_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/rotate/useExtMod", 0],
			["float", "/TXDrawSVG_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawSVG_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/rotateMultiply", [
			["float", "/TXDrawSVG_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawSVG_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawSVG_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawSVG_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/scaleX", [
			["float", "/TXDrawSVG_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawSVG_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/scaleX/useExtMod", 0],
			["float", "/TXDrawSVG_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/scaleX/softMin", 0, -100, 100],
			["float", "/TXDrawSVG_1/scaleX/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawSVG_1/scaleY", [
			["float", "/TXDrawSVG_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawSVG_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawSVG_1/scaleY/useExtMod", 0],
			["float", "/TXDrawSVG_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawSVG_1/scaleY/softMin", 0, -100, 100],
			["float", "/TXDrawSVG_1/scaleY/softMax", 10, -100, 100],
		]],
		["bool_int", "/TXDrawSVG_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawSVG_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Draw$Active & Scale", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",
					"scaleX", "scaleY","useScaleXForScaleY", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ",
					"useSamplePosForDrawPos","anchorX", "anchorY", "rotate", "rotateX", "rotateY", "rotateZ",
					"rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"SVG File";
	}

}


