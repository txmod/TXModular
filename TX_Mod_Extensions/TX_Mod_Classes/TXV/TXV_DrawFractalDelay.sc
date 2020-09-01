// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawFractalDelay : TXV_Module {
	classvar <defaultName = "TXV FractalDelay";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	//  for ALL branches - add text array to ... "branchRule/fixedValue"
	// , ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
	// "Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]

	// for ALL branches - add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawFractalDelay_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/drawActive", [
			["bool_int", "/TXDrawFractalDelay_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/drawActive/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawFractalDelay_1/maxWidthHeightRule", 0, 0, 5,
			["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawFractalDelay_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawFractalDelay_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/fractalTreeLevels", [
			["int", "/TXDrawFractalDelay_1/fractalTreeLevels/fixedValue", 2, 1, 10],
			["float", "/TXDrawFractalDelay_1/fractalTreeLevels/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/fractalTreeLevels/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/fractalTreeLevels/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/fractalTreeLevels/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/fractalTreeLevels/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/fractalTreeBranches", [
			["int", "/TXDrawFractalDelay_1/fractalTreeBranches/fixedValue", 2, 1, 10],
			["float", "/TXDrawFractalDelay_1/fractalTreeBranches/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/fractalTreeBranches/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/fractalTreeBranches/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/fractalTreeBranches/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/fractalTreeBranches/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/cellWidth", [
			["float", "/TXDrawFractalDelay_1/cellWidth/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawFractalDelay_1/cellWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/cellWidth/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/cellWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/cellWidth/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/cellWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/cellHeight", [
			["float", "/TXDrawFractalDelay_1/cellHeight/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawFractalDelay_1/cellHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/cellHeight/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/cellHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/cellHeight/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/cellHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/randomSeed", [
			["int", "/TXDrawFractalDelay_1/randomSeed/fixedValue", 100, 0, 10000],
			["float", "/TXDrawFractalDelay_1/randomSeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/randomSeed/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/randomSeed/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/randomSeed/softMin", 0, 0, 10000],
			["int", "/TXDrawFractalDelay_1/randomSeed/softMax", 10000, 0, 10000],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/maxLevelDelayFrames", [
			["int", "/TXDrawFractalDelay_1/maxLevelDelayFrames/fixedValue", 60, 0, 100],
			["float", "/TXDrawFractalDelay_1/maxLevelDelayFrames/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/maxLevelDelayFrames/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/maxLevelDelayFrames/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/maxLevelDelayFrames/softMin", 0, 0, 1000],
			["int", "/TXDrawFractalDelay_1/maxLevelDelayFrames/softMax", 100, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level1DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level1DelayRatio/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level1DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level1DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level1DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level1DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level1DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level2DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level2DelayRatio/fixedValue", 0.11111, 0, 1],
			["float", "/TXDrawFractalDelay_1/level2DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level2DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level2DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level2DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level2DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level3DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level3DelayRatio/fixedValue", 0.22222, 0, 1],
			["float", "/TXDrawFractalDelay_1/level3DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level3DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level3DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level3DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level3DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level4DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level4DelayRatio/fixedValue", 0.33333, 0, 1],
			["float", "/TXDrawFractalDelay_1/level4DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level4DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level4DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level4DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level4DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level5DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level5DelayRatio/fixedValue", 0.44444, 0, 1],
			["float", "/TXDrawFractalDelay_1/level5DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level5DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level5DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level5DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level5DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level6DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level6DelayRatio/fixedValue", 0.555555, 0, 1],
			["float", "/TXDrawFractalDelay_1/level6DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level6DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level6DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level6DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level6DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level7DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level7DelayRatio/fixedValue", 0.66666, 0, 1],
			["float", "/TXDrawFractalDelay_1/level7DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level7DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level7DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level7DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level7DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level8DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level8DelayRatio/fixedValue", 0.77777, 0, 1],
			["float", "/TXDrawFractalDelay_1/level8DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level8DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level8DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level8DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level8DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level9DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level9DelayRatio/fixedValue", 0.88888, 0, 1],
			["float", "/TXDrawFractalDelay_1/level9DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level9DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level9DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level9DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level9DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level10DelayRatio", [
			["float", "/TXDrawFractalDelay_1/level10DelayRatio/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/level10DelayRatio/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level10DelayRatio/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level10DelayRatio/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level10DelayRatio/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level10DelayRatio/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level1RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level1RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level1RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level1RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level1RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level1RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level1RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level2RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level2RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level2RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level2RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level2RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level2RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level2RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level3RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level3RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level3RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level3RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level3RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level3RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level3RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level4RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level4RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level4RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level4RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level4RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level4RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level4RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level5RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level5RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level5RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level5RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level5RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level5RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level5RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level6RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level6RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level6RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level6RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level6RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level6RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level6RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level7RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level7RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level7RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level7RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level7RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level7RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level7RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level8RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level8RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level8RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level8RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level8RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level8RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level8RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level9RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level9RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level9RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level9RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level9RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level9RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level9RandDelayMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/level10RandDelayMix", [
			["float", "/TXDrawFractalDelay_1/level10RandDelayMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level10RandDelayMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/level10RandDelayMix/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/level10RandDelayMix/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level10RandDelayMix/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/level10RandDelayMix/softMax", 1, 0, 1],
		]],
		// BRANCHES
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch01_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch01_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch01_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch01_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch01_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch01_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch01_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch01_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch01_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch01_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch01_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch01_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch01_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch01_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch01_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch01_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch01_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch01_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch01_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch01_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch01_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch01_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch01_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch01_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch01_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch01_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch01_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch01_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch01_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch01_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch01_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch01_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch01_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch01_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch01_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch01_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch01_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch01_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch01_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch01_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch01_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch01_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_angle", [
			["float", "/TXDrawFractalDelay_1/branch01_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch01_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch01_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch01_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch01_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch01_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch01_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch01_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch01_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch01_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch01_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch01_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch01_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch02_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch02_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch02_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch02_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch02_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch02_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch02_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch02_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch02_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch02_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch02_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch02_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch02_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch02_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch02_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch02_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch02_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch02_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch02_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch02_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch02_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch02_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch02_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch02_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch02_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch02_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch02_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch02_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch02_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch02_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch02_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch02_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch02_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch02_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch02_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch02_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch02_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch02_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch02_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch02_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch02_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch02_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_angle", [
			["float", "/TXDrawFractalDelay_1/branch02_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch02_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch02_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch02_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch02_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch02_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch02_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch02_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch02_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch02_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch02_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch02_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch02_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch03_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch03_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch03_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch03_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch03_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch03_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch03_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch03_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch03_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch03_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch03_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch03_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch03_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch03_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch03_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch03_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch03_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch03_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch03_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch03_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch03_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch03_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch03_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch03_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch03_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch03_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch03_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch03_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch03_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch03_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch03_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch03_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch03_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch03_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch03_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch03_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch03_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch03_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch03_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch03_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch03_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch03_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_angle", [
			["float", "/TXDrawFractalDelay_1/branch03_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch03_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch03_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch03_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch03_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch03_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch03_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch03_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch03_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch03_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch03_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch03_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch03_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch04_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch04_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch04_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch04_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch04_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch04_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch04_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch04_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch04_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch04_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch04_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch04_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch04_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch04_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch04_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch04_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch04_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch04_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch04_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch04_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch04_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch04_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch04_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch04_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch04_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch04_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch04_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch04_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch04_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch04_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch04_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch04_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch04_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch04_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch04_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch04_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch04_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch04_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch04_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch04_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch04_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch04_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_angle", [
			["float", "/TXDrawFractalDelay_1/branch04_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch04_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch04_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch04_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch04_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch04_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch04_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch04_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch04_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch04_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch04_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch04_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch04_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch05_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch05_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch05_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch05_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch05_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch05_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch05_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch05_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch05_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch05_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch05_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch05_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch05_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch05_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch05_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch05_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch05_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch05_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch05_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch05_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch05_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch05_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch05_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch05_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch05_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch05_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch05_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch05_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch05_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch05_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch05_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch05_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch05_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch05_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch05_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch05_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch05_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch05_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch05_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch05_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch05_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch05_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_angle", [
			["float", "/TXDrawFractalDelay_1/branch05_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch05_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch05_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch05_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch05_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch05_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch05_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch05_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch05_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch05_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch05_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch05_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch05_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch06_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch06_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch06_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch06_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch06_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch06_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch06_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch06_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch06_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch06_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch06_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch06_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch06_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch06_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch06_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch06_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch06_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch06_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch06_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch06_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch06_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch06_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch06_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch06_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch06_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch06_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch06_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch06_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch06_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch06_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch06_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch06_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch06_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch06_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch06_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch06_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch06_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch06_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch06_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch06_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch06_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch06_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_angle", [
			["float", "/TXDrawFractalDelay_1/branch06_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch06_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch06_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch06_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch06_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch06_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch06_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch06_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch06_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch06_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch06_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch06_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch06_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch07_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch07_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch07_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch07_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch07_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch07_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch07_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch07_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch07_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch07_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch07_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch07_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch07_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch07_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch07_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch07_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch07_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch07_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch07_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch07_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch07_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch07_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch07_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch07_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch07_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch07_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch07_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch07_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch07_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch07_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch07_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch07_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch07_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch07_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch07_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch07_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch07_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch07_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch07_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch07_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch07_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch07_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_angle", [
			["float", "/TXDrawFractalDelay_1/branch07_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch07_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch07_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch07_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch07_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch07_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch07_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch07_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch07_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch07_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch07_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch07_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch07_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch08_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch08_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch08_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch08_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch08_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch08_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch08_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch08_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch08_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch08_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch08_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch08_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch08_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch08_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch08_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch08_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch08_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch08_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch08_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch08_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch08_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch08_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch08_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch08_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch08_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch08_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch08_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch08_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch08_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch08_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch08_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch08_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch08_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch08_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch08_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch08_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch08_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch08_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch08_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch08_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch08_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch08_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_angle", [
			["float", "/TXDrawFractalDelay_1/branch08_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch08_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch08_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch08_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch08_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch08_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch08_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch08_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch08_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch08_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch08_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch08_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch08_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch09_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch09_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch09_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch09_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch09_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch09_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch09_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch09_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch09_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch09_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch09_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch09_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch09_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch09_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch09_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch09_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch09_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch09_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch09_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch09_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch09_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch09_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch09_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch09_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch09_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch09_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch09_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch09_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch09_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch09_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch09_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch09_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch09_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch09_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch09_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch09_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch09_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch09_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch09_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch09_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch09_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch09_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_angle", [
			["float", "/TXDrawFractalDelay_1/branch09_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch09_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch09_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch09_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch09_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch09_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch09_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch09_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch09_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch09_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch09_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch09_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch09_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch10_branchIfTopLevel", [
			["bool_int", "/TXDrawFractalDelay_1/branch10_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch10_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch10_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractalDelay_1/branch10_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch10_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch10_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractalDelay_1/branch10_applyOffsetLast", [
			["bool_int", "/TXDrawFractalDelay_1/branch10_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractalDelay_1/branch10_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch10_branchRule", [
			["int", "/TXDrawFractalDelay_1/branch10_branchRule/fixedValue", 0, 0, 4,
				["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
					"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractalDelay_1/branch10_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_branchRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch10_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractalDelay_1/branch10_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch10_branchRuleComparator", [
			["int", "/TXDrawFractalDelay_1/branch10_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractalDelay_1/branch10_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch10_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractalDelay_1/branch10_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractalDelay_1/branch10_drawLayersRule", [
			["int", "/TXDrawFractalDelay_1/branch10_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractalDelay_1/branch10_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractalDelay_1/branch10_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractalDelay_1/branch10_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_offsetX", [
			["float", "/TXDrawFractalDelay_1/branch10_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch10_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_offsetX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch10_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_offsetY", [
			["float", "/TXDrawFractalDelay_1/branch10_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch10_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_offsetY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractalDelay_1/branch10_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_anchorX", [
			["float", "/TXDrawFractalDelay_1/branch10_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_anchorX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_anchorY", [
			["float", "/TXDrawFractalDelay_1/branch10_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_anchorY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_anchorY/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractalDelay_1/branch10_useScaleXForScaleY", 0],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_scaleX", [
			["float", "/TXDrawFractalDelay_1/branch10_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch10_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_scaleX/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch10_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_scaleXRandom", [
			["float", "/TXDrawFractalDelay_1/branch10_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_scaleY", [
			["float", "/TXDrawFractalDelay_1/branch10_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch10_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_scaleY/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractalDelay_1/branch10_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_scaleYRandom", [
			["float", "/TXDrawFractalDelay_1/branch10_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_angle", [
			["float", "/TXDrawFractalDelay_1/branch10_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch10_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_angle/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractalDelay_1/branch10_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_angleLevelDelta", [
			["float", "/TXDrawFractalDelay_1/branch10_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch10_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractalDelay_1/branch10_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_angleRandom", [
			["float", "/TXDrawFractalDelay_1/branch10_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_branchProbability", [
			["float", "/TXDrawFractalDelay_1/branch10_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractalDelay_1/branch10_subBranchProbability", [
			["float", "/TXDrawFractalDelay_1/branch10_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractalDelay_1/branch10_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractalDelay_1/branch10_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractalDelay_1/branch10_subBranchProbability/softMax", 1, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawFractalDelay_1 ---------- */
		// empty
	];}


	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			// ["Global", {arg parameterName;
			// 	var arrNames = ["fractalTreeLevels", "fractalTreeBranches", "cellWidth", "cellHeight", "randomSeed", ];
			// 	arrNames.indexOfEqual(parameterName).notNil;
			// }],
			["Level Delays", {arg parameterName;
				parameterName.containsi("maxLevelDelayFrames")
				|| parameterName.containsi("DelayRatio")
				|| parameterName.containsi("RandDelayMix")}],
			["Branch 1$Options & Probability", {arg parameterName;
				parameterName.containsi("branch01_")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],

			["Branch 1$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch01_")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 1$Scale", {arg parameterName;
				parameterName.containsi("branch01_")
				&& parameterName.containsi("scale")
				;}],
			["Branch 1$Rotate", {arg parameterName;
				parameterName.containsi("branch01_")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 2$Options & Probability", {arg parameterName;
				parameterName.containsi("branch02")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 2$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch02")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 2$Scale", {arg parameterName;
				parameterName.containsi("branch02")
				&& parameterName.containsi("scale")
				;}],
			["Branch 2$Rotate", {arg parameterName;
				parameterName.containsi("branch02")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 3$Options & Probability", {arg parameterName;
				parameterName.containsi("branch03")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 3$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch03")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 3$Scale", {arg parameterName;
				parameterName.containsi("branch03")
				&& parameterName.containsi("scale")
				;}],
			["Branch 3$Rotate", {arg parameterName;
				parameterName.containsi("branch03")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 4$Options & Probability", {arg parameterName;
				parameterName.containsi("branch04")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 4$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch04")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 4$Scale", {arg parameterName;
				parameterName.containsi("branch04")
				&& parameterName.containsi("scale")
				;}],
			["Branch 4$Rotate", {arg parameterName;
				parameterName.containsi("branch04")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 5$Options & Probability", {arg parameterName;
				parameterName.containsi("branch05")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 5$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch05")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 5$Scale", {arg parameterName;
				parameterName.containsi("branch05")
				&& parameterName.containsi("scale")
				;}],
			["Branch 5$Rotate", {arg parameterName;
				parameterName.containsi("branch05")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 6$Options & Probability", {arg parameterName;
				parameterName.containsi("branch06")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 6$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch06")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 6$Scale", {arg parameterName;
				parameterName.containsi("branch06")
				&& parameterName.containsi("scale")
				;}],
			["Branch 6$Rotate", {arg parameterName;
				parameterName.containsi("branch06")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 7$Options & Probability", {arg parameterName;
				parameterName.containsi("branch07")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 7$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch07")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 7$Scale", {arg parameterName;
				parameterName.containsi("branch07")
				&& parameterName.containsi("scale")
				;}],
			["Branch 7$Rotate", {arg parameterName;
				parameterName.containsi("branch07")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 8$Options & Probability", {arg parameterName;
				parameterName.containsi("branch08")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 8$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch08")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 8$Scale", {arg parameterName;
				parameterName.containsi("branch08")
				&& parameterName.containsi("scale")
				;}],
			["Branch 8$Rotate", {arg parameterName;
				parameterName.containsi("branch08")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 9$Options & Probability", {arg parameterName;
				parameterName.containsi("branch09")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 9$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch09")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 9$Scale", {arg parameterName;
				parameterName.containsi("branch09")
				&& parameterName.containsi("scale")
				;}],
			["Branch 9$Rotate", {arg parameterName;
				parameterName.containsi("branch09")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			["Branch 10$Options & Probability", {arg parameterName;
				parameterName.containsi("branch10_")
				&& not(parameterName.containsi("offset"))
				&& not(parameterName.containsi("anchor"))
				&& not(parameterName.containsi("scale"))
				&& not(parameterName.containsi("angle"))
				&& not(parameterName.containsi("rotate"))
				;}],
			["Branch 10$Offset & Anchor", {arg parameterName;
				parameterName.containsi("branch10_")
				&& (parameterName.containsi("offset")
					|| parameterName.containsi("anchor"))
				;}],
			["Branch 10$Scale", {arg parameterName;
				parameterName.containsi("branch10_")
				&& parameterName.containsi("scale")
				;}],
			["Branch 10$Rotate", {arg parameterName;
				parameterName.containsi("branch10_")
				&& (parameterName.containsi("angle")
					|| parameterName.containsi("rotate"))
				;}],
			// ["Draw", {arg parameterName;
			// 	var drawParameterNames = [ "drawActive", "maxWidthHeightRule", "customMaxWidth", "customMaxHeight", ];
			// 	drawParameterNames.indexOfEqual(parameterName).notNil;
			// }],
		];
	}

	getDefaultParamSectionName {  // override
		^"Global";
	}

}


