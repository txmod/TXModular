// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Draw2DShape : TXV_Module {
	classvar <defaultName = "TXV Shape 2D";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."shapeType/fixedValue":
	// , ["Circle (width, circleResolution)", "Ellipse (width, height, circleResolution)", "Square (width)", "Rectangle (width, height)", "Round Rectangle (width, height, roundness, circleResolution)", "Star (width, height, circleResolution)", "Triangle 1 (Isosceles) (width, height)", "Triangle 2 (right) (width, height)", "Diamond (width, height)", "Crescent (width, height, thickness, circleResolution)", "Ring (width, thickness, circleResolution)"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDraw2DShape_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDraw2DShape_1/drawActive", [
			["bool_int", "/TXDraw2DShape_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDraw2DShape_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDraw2DShape_1/drawActive/useExtMod", 0],
			["float", "/TXDraw2DShape_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDraw2DShape_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDraw2DShape_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDraw2DShape_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXDraw2DShape_1/shapeType", [
			["int", "/TXDraw2DShape_1/shapeType/fixedValue", 0, 0, 10, ["Circle (width, circleResolution)", "Ellipse (width, height, circleResolution)", "Square (width)", "Rectangle (width, height)", "Round Rectangle (width, height, roundness, circleResolution)", "Star (width, height, circleResolution)", "Triangle 1 (Isosceles) (width, height)", "Triangle 2 (right) (width, height)", "Diamond (width, height)", "Crescent (width, height, thickness, circleResolution)", "Ring (width, thickness, circleResolution)"]],
			["float", "/TXDraw2DShape_1/shapeType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/shapeType/useExtMod", 0],
			["float", "/TXDraw2DShape_1/shapeType/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShape_1/shapeType/softMin", 0, 0, 10],
			["int", "/TXDraw2DShape_1/shapeType/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDraw2DShape_1/lineWidth", [
			["int", "/TXDraw2DShape_1/lineWidth/fixedValue", 2, 0, 10],
			["float", "/TXDraw2DShape_1/lineWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/lineWidth/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineWidth/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShape_1/lineWidth/softMin", 0, 0, 10],
			["int", "/TXDraw2DShape_1/lineWidth/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShape_1/fillShape", [
			["bool_int", "/TXDraw2DShape_1/fillShape/fixedValue", 1],
			["bool_int", "/TXDraw2DShape_1/fillShape/fixedModMix", 0],
			["bool_int", "/TXDraw2DShape_1/fillShape/useExtMod", 0],
			["float", "/TXDraw2DShape_1/fillShape/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/width", [
			["float", "/TXDraw2DShape_1/width/fixedValue", 0.05, 0, 1],
			["float", "/TXDraw2DShape_1/width/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/width/useExtMod", 0],
			["float", "/TXDraw2DShape_1/width/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/width/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/width/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/height", [
			["float", "/TXDraw2DShape_1/height/fixedValue", 0.1, 0, 1],
			["float", "/TXDraw2DShape_1/height/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/height/useExtMod", 0],
			["float", "/TXDraw2DShape_1/height/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/height/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/height/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDraw2DShape_1/useMaxWidthToScaleHeight", 0],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/roundness", [
			["float", "/TXDraw2DShape_1/roundness/fixedValue", 0.2, 0, 1],
			["float", "/TXDraw2DShape_1/roundness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/roundness/useExtMod", 0],
			["float", "/TXDraw2DShape_1/roundness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/roundness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/roundness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/thickness", [
			["float", "/TXDraw2DShape_1/thickness/fixedValue", 0.2, 0, 1],
			["float", "/TXDraw2DShape_1/thickness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/thickness/useExtMod", 0],
			["float", "/TXDraw2DShape_1/thickness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/thickness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/thickness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDraw2DShape_1/circleResolution", [
			["int", "/TXDraw2DShape_1/circleResolution/fixedValue", 32, 3, 128],
			["float", "/TXDraw2DShape_1/circleResolution/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/circleResolution/useExtMod", 0],
			["float", "/TXDraw2DShape_1/circleResolution/extModValue", 0, 0, 1],
			["int", "/TXDraw2DShape_1/circleResolution/softMin", 3, 3, 1024],
			["int", "/TXDraw2DShape_1/circleResolution/softMax", 128, 3, 1024],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/colorHue", [
			["float", "/TXDraw2DShape_1/colorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/colorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/colorHue/useExtMod", 0],
			["float", "/TXDraw2DShape_1/colorHue/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorHue/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/colorHueRotate", [
			["float", "/TXDraw2DShape_1/colorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/colorHueRotate/useExtMod", 0],
			["float", "/TXDraw2DShape_1/colorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/colorSaturation", [
			["float", "/TXDraw2DShape_1/colorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDraw2DShape_1/colorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/colorSaturation/useExtMod", 0],
			["float", "/TXDraw2DShape_1/colorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorSaturation/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/colorBrightness", [
			["float", "/TXDraw2DShape_1/colorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/colorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/colorBrightness/useExtMod", 0],
			["float", "/TXDraw2DShape_1/colorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorBrightness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/colorAlpha", [
			["float", "/TXDraw2DShape_1/colorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDraw2DShape_1/colorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/colorAlpha/useExtMod", 0],
			["float", "/TXDraw2DShape_1/colorAlpha/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorAlpha/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/colorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShape_1/colorHSBAsRGB", [
			["bool_int", "/TXDraw2DShape_1/colorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDraw2DShape_1/colorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDraw2DShape_1/colorHSBAsRGB/useExtMod", 0],
			["float", "/TXDraw2DShape_1/colorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShape_1/useFillColorForLineColor", [
			["bool_int", "/TXDraw2DShape_1/useFillColorForLineColor/fixedValue", 0],
			["bool_int", "/TXDraw2DShape_1/useFillColorForLineColor/fixedModMix", 0],
			["bool_int", "/TXDraw2DShape_1/useFillColorForLineColor/useExtMod", 0],
			["float", "/TXDraw2DShape_1/useFillColorForLineColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShape_1/swapFillColorAndLineColor", [
			["bool_int", "/TXDraw2DShape_1/swapFillColorAndLineColor/fixedValue", 0],
			["bool_int", "/TXDraw2DShape_1/swapFillColorAndLineColor/fixedModMix", 0],
			["bool_int", "/TXDraw2DShape_1/swapFillColorAndLineColor/useExtMod", 0],
			["float", "/TXDraw2DShape_1/swapFillColorAndLineColor/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/lineColorHue", [
			["float", "/TXDraw2DShape_1/lineColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/lineColorHue/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineColorHue/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorHue/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/lineColorHueRotate", [
			["float", "/TXDraw2DShape_1/lineColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/lineColorHueRotate/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/lineColorSaturation", [
			["float", "/TXDraw2DShape_1/lineColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/lineColorSaturation/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/lineColorBrightness", [
			["float", "/TXDraw2DShape_1/lineColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/lineColorBrightness/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/lineColorAlpha", [
			["float", "/TXDraw2DShape_1/lineColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/lineColorAlpha/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorAlpha/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/lineColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDraw2DShape_1/lineColorHSBAsRGB", [
			["bool_int", "/TXDraw2DShape_1/lineColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDraw2DShape_1/lineColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDraw2DShape_1/lineColorHSBAsRGB/useExtMod", 0],
			["float", "/TXDraw2DShape_1/lineColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/positionX", [
			["float", "/TXDraw2DShape_1/positionX/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/positionX/useExtMod", 0],
			["float", "/TXDraw2DShape_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/positionX/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/positionX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/positionY", [
			["float", "/TXDraw2DShape_1/positionY/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/positionY/useExtMod", 0],
			["float", "/TXDraw2DShape_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/positionY/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/positionY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/rotate", [
			["float", "/TXDraw2DShape_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDraw2DShape_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/rotate/useExtMod", 0],
			["float", "/TXDraw2DShape_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDraw2DShape_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/rotateMultiply", [
			["float", "/TXDraw2DShape_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDraw2DShape_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDraw2DShape_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDraw2DShape_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/anchorX", [
			["float", "/TXDraw2DShape_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/anchorX/useExtMod", 0],
			["float", "/TXDraw2DShape_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/anchorY", [
			["float", "/TXDraw2DShape_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDraw2DShape_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/anchorY/useExtMod", 0],
			["float", "/TXDraw2DShape_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDraw2DShape_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/scaleX", [
			["float", "/TXDraw2DShape_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDraw2DShape_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/scaleX/useExtMod", 0],
			["float", "/TXDraw2DShape_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDraw2DShape_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDraw2DShape_1/scaleY", [
			["float", "/TXDraw2DShape_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDraw2DShape_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDraw2DShape_1/scaleY/useExtMod", 0],
			["float", "/TXDraw2DShape_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDraw2DShape_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDraw2DShape_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDraw2DShape_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDraw2DShape_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Fill Color", {arg parameterName;
				var arrNames = ["colorHue", "colorHueRotate", "colorSaturation", "colorBrightness",
					"colorAlpha", "colorHSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Outline Color", {arg parameterName;
				var arrNames = ["useFillColorForLineColor", "swapFillColorAndLineColor",
					"lineColorHue", "lineColorHueRotate", "lineColorSaturation", "lineColorBrightness",
					"lineColorAlpha", "lineColorHSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Active & Max Size", {arg parameterName;
				var arrNames = ["drawActive", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ",
				"anchorX", "anchorY", "rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Shape";
	}

}


