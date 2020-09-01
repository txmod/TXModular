// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_ColorSampler : TXV_Module {
	classvar <defaultName = "TXV Color Sampler";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXColorSampler_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		["bool_int", "/TXColorSampler_1/useExternalSourceImage", 0],
		[ "modParameterGroupBool", "/TXColorSampler_1/sample1Active", [
			["bool_int", "/TXColorSampler_1/sample1Active/fixedValue", 1],
			["bool_int", "/TXColorSampler_1/sample1Active/fixedModMix", 0],
			["bool_int", "/TXColorSampler_1/sample1Active/useExtMod", 0],
			["float", "/TXColorSampler_1/sample1Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample1PosX", [
			["float", "/TXColorSampler_1/sample1PosX/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample1PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample1PosX/useExtMod", 0],
			["float", "/TXColorSampler_1/sample1PosX/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample1PosX/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample1PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample1PosY", [
			["float", "/TXColorSampler_1/sample1PosY/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample1PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample1PosY/useExtMod", 0],
			["float", "/TXColorSampler_1/sample1PosY/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample1PosY/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample1PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/color1SmoothTime", [
			["float", "/TXColorSampler_1/color1SmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXColorSampler_1/color1SmoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/color1SmoothTime/useExtMod", 0],
			["float", "/TXColorSampler_1/color1SmoothTime/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/color1SmoothTime/softMin", 0, 0, 60],
			["float", "/TXColorSampler_1/color1SmoothTime/softMax", 2, 0, 60],
		]],
		[ "modParameterGroupBool", "/TXColorSampler_1/sample2Active", [
			["bool_int", "/TXColorSampler_1/sample2Active/fixedValue", 1],
			["bool_int", "/TXColorSampler_1/sample2Active/fixedModMix", 0],
			["bool_int", "/TXColorSampler_1/sample2Active/useExtMod", 0],
			["float", "/TXColorSampler_1/sample2Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample2PosX", [
			["float", "/TXColorSampler_1/sample2PosX/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample2PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample2PosX/useExtMod", 0],
			["float", "/TXColorSampler_1/sample2PosX/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample2PosX/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample2PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample2PosY", [
			["float", "/TXColorSampler_1/sample2PosY/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample2PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample2PosY/useExtMod", 0],
			["float", "/TXColorSampler_1/sample2PosY/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample2PosY/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample2PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/color2SmoothTime", [
			["float", "/TXColorSampler_1/color2SmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXColorSampler_1/color2SmoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/color2SmoothTime/useExtMod", 0],
			["float", "/TXColorSampler_1/color2SmoothTime/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/color2SmoothTime/softMin", 0, 0, 60],
			["float", "/TXColorSampler_1/color2SmoothTime/softMax", 2, 0, 60],
		]],
		[ "modParameterGroupBool", "/TXColorSampler_1/sample3Active", [
			["bool_int", "/TXColorSampler_1/sample3Active/fixedValue", 1],
			["bool_int", "/TXColorSampler_1/sample3Active/fixedModMix", 0],
			["bool_int", "/TXColorSampler_1/sample3Active/useExtMod", 0],
			["float", "/TXColorSampler_1/sample3Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample3PosX", [
			["float", "/TXColorSampler_1/sample3PosX/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample3PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample3PosX/useExtMod", 0],
			["float", "/TXColorSampler_1/sample3PosX/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample3PosX/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample3PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample3PosY", [
			["float", "/TXColorSampler_1/sample3PosY/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample3PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample3PosY/useExtMod", 0],
			["float", "/TXColorSampler_1/sample3PosY/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample3PosY/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample3PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/color3SmoothTime", [
			["float", "/TXColorSampler_1/color3SmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXColorSampler_1/color3SmoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/color3SmoothTime/useExtMod", 0],
			["float", "/TXColorSampler_1/color3SmoothTime/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/color3SmoothTime/softMin", 0, 0, 60],
			["float", "/TXColorSampler_1/color3SmoothTime/softMax", 2, 0, 60],
		]],
		[ "modParameterGroupBool", "/TXColorSampler_1/sample4Active", [
			["bool_int", "/TXColorSampler_1/sample4Active/fixedValue", 1],
			["bool_int", "/TXColorSampler_1/sample4Active/fixedModMix", 0],
			["bool_int", "/TXColorSampler_1/sample4Active/useExtMod", 0],
			["float", "/TXColorSampler_1/sample4Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample4PosX", [
			["float", "/TXColorSampler_1/sample4PosX/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample4PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample4PosX/useExtMod", 0],
			["float", "/TXColorSampler_1/sample4PosX/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample4PosX/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample4PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/sample4PosY", [
			["float", "/TXColorSampler_1/sample4PosY/fixedValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample4PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/sample4PosY/useExtMod", 0],
			["float", "/TXColorSampler_1/sample4PosY/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/sample4PosY/softMin", 0, 0, 1],
			["float", "/TXColorSampler_1/sample4PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXColorSampler_1/color4SmoothTime", [
			["float", "/TXColorSampler_1/color4SmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXColorSampler_1/color4SmoothTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXColorSampler_1/color4SmoothTime/useExtMod", 0],
			["float", "/TXColorSampler_1/color4SmoothTime/extModValue", 0, 0, 1],
			["float", "/TXColorSampler_1/color4SmoothTime/softMin", 0, 0, 60],
			["float", "/TXColorSampler_1/color4SmoothTime/softMax", 2, 0, 60],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXColorSampler_1 ---------- */
		"color1Alpha",
		"color1Blue",
		"color1Brightness",
		"color1Green",
		"color1Hue",
		"color1Red",
		"color1Saturation",
		"color2Alpha",
		"color2Blue",
		"color2Brightness",
		"color2Green",
		"color2Hue",
		"color2Red",
		"color2Saturation",
		"color3Alpha",
		"color3Blue",
		"color3Brightness",
		"color3Green",
		"color3Hue",
		"color3Red",
		"color3Saturation",
		"color4Alpha",
		"color4Blue",
		"color4Brightness",
		"color4Green",
		"color4Hue",
		"color4Red",
		"color4Saturation",
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Sample 1", {arg parameterName;
				var arrNames = [ "sample1Active", "sample1PosX", "sample1PosY", "color1SmoothTime", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Sample 2", {arg parameterName;
				var arrNames = [ "sample2Active", "sample2PosX", "sample2PosY", "color2SmoothTime", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Sample 3", {arg parameterName;
				var arrNames = [ "sample3Active", "sample3PosX", "sample3PosY", "color3SmoothTime", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Sample 4", {arg parameterName;
				var arrNames = [ "sample4Active", "sample4PosX", "sample4PosY", "color4SmoothTime" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Image";
	}

}


