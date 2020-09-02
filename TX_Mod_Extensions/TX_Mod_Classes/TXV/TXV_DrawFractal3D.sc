// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawFractal3D : TXV_Module {
	classvar <defaultName = "TXV Fractal3D";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maxDepthRule":
	// , ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]

	//  for ALL branches - add text array to ... "branchRule/fixedValue"
	// , ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator", "Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]

	// for ALL branches - add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawFractal3D_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/drawActive", [
			["bool_int", "/TXDrawFractal3D_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/drawActive/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawFractal3D_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawShape3D_1/maxDepthRule", 0, 0, 4, ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]],
		["int", "/TXDrawFractal3D_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawFractal3D_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXDrawFractal3D_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/fractalTreeLevels", [
			["int", "/TXDrawFractal3D_1/fractalTreeLevels/fixedValue", 2, 1, 10],
			["float", "/TXDrawFractal3D_1/fractalTreeLevels/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/fractalTreeLevels/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/fractalTreeLevels/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/fractalTreeLevels/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/fractalTreeLevels/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/fractalTreeBranches", [
			["int", "/TXDrawFractal3D_1/fractalTreeBranches/fixedValue", 2, 1, 10],
			["float", "/TXDrawFractal3D_1/fractalTreeBranches/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/fractalTreeBranches/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/fractalTreeBranches/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/fractalTreeBranches/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/fractalTreeBranches/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/cellWidth", [
			["float", "/TXDrawFractal3D_1/cellWidth/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawFractal3D_1/cellWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/cellWidth/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/cellWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/cellWidth/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/cellWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/cellHeight", [
			["float", "/TXDrawFractal3D_1/cellHeight/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawFractal3D_1/cellHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/cellHeight/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/cellHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/cellHeight/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/cellHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/cellDepth", [
			["float", "/TXDrawFractal3D_1/cellDepth/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawFractal3D_1/cellDepth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/cellDepth/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/cellDepth/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/cellDepth/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/cellDepth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/randomSeed", [
			["int", "/TXDrawFractal3D_1/randomSeed/fixedValue", 100, 0, 10000],
			["float", "/TXDrawFractal3D_1/randomSeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/randomSeed/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/randomSeed/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/randomSeed/softMin", 0, 0, 10000],
			["int", "/TXDrawFractal3D_1/randomSeed/softMax", 10000, 0, 10000],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch01_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch01_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch01_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch01_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch01_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch01_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch01_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch01_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch01_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch01_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch01_branchRule", [
			["int", "/TXDrawFractal3D_1/branch01_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch01_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch01_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch01_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch01_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch01_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch01_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch01_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch01_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch01_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch01_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch01_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch01_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch01_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_offsetX", [
			["float", "/TXDrawFractal3D_1/branch01_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch01_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch01_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_offsetY", [
			["float", "/TXDrawFractal3D_1/branch01_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch01_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch01_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch01_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch01_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch01_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_anchorX", [
			["float", "/TXDrawFractal3D_1/branch01_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_anchorY", [
			["float", "/TXDrawFractal3D_1/branch01_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch01_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch01_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch01_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleX", [
			["float", "/TXDrawFractal3D_1/branch01_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch01_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch01_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch01_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleY", [
			["float", "/TXDrawFractal3D_1/branch01_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch01_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch01_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch01_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch01_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch01_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch01_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch01_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisX/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisZ/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch01_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_angle", [
			["float", "/TXDrawFractal3D_1/branch01_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch01_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch01_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch01_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch01_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch01_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch01_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch01_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch01_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch01_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch01_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch01_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch01_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch02_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch02_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch02_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch02_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch02_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch02_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch02_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch02_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch02_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch02_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch02_branchRule", [
			["int", "/TXDrawFractal3D_1/branch02_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch02_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch02_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch02_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch02_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch02_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch02_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch02_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch02_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch02_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch02_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch02_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch02_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch02_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_offsetX", [
			["float", "/TXDrawFractal3D_1/branch02_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch02_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch02_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_offsetY", [
			["float", "/TXDrawFractal3D_1/branch02_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch02_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch02_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch02_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch02_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch02_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_anchorX", [
			["float", "/TXDrawFractal3D_1/branch02_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_anchorY", [
			["float", "/TXDrawFractal3D_1/branch02_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch02_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch02_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch02_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleX", [
			["float", "/TXDrawFractal3D_1/branch02_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch02_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch02_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch02_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleY", [
			["float", "/TXDrawFractal3D_1/branch02_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch02_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch02_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch02_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch02_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch02_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch02_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch02_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch02_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_angle", [
			["float", "/TXDrawFractal3D_1/branch02_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch02_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch02_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch02_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch02_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch02_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch02_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch02_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch02_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch02_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch02_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch02_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch02_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch03_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch03_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch03_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch03_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch03_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch03_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch03_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch03_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch03_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch03_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch03_branchRule", [
			["int", "/TXDrawFractal3D_1/branch03_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch03_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch03_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch03_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch03_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch03_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch03_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch03_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch03_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch03_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch03_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch03_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch03_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch03_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_offsetX", [
			["float", "/TXDrawFractal3D_1/branch03_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch03_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch03_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_offsetY", [
			["float", "/TXDrawFractal3D_1/branch03_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch03_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch03_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch03_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch03_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch03_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_anchorX", [
			["float", "/TXDrawFractal3D_1/branch03_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_anchorY", [
			["float", "/TXDrawFractal3D_1/branch03_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch03_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch03_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch03_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleX", [
			["float", "/TXDrawFractal3D_1/branch03_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch03_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch03_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch03_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleY", [
			["float", "/TXDrawFractal3D_1/branch03_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch03_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch03_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch03_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch03_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch03_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch03_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch03_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch03_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_angle", [
			["float", "/TXDrawFractal3D_1/branch03_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch03_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch03_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch03_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch03_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch03_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch03_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch03_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch03_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch03_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch03_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch03_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch03_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch04_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch04_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch04_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch04_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch04_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch04_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch04_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch04_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch04_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch04_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch04_branchRule", [
			["int", "/TXDrawFractal3D_1/branch04_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch04_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch04_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch04_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch04_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch04_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch04_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch04_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch04_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch04_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch04_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch04_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch04_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch04_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_offsetX", [
			["float", "/TXDrawFractal3D_1/branch04_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch04_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch04_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_offsetY", [
			["float", "/TXDrawFractal3D_1/branch04_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch04_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch04_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch04_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch04_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch04_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_anchorX", [
			["float", "/TXDrawFractal3D_1/branch04_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_anchorY", [
			["float", "/TXDrawFractal3D_1/branch04_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch04_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch04_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch04_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleX", [
			["float", "/TXDrawFractal3D_1/branch04_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch04_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch04_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch04_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleY", [
			["float", "/TXDrawFractal3D_1/branch04_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch04_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch04_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch04_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch04_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch04_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch04_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch04_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch04_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_angle", [
			["float", "/TXDrawFractal3D_1/branch04_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch04_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch04_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch04_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch04_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch04_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch04_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch04_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch04_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch04_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch04_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch04_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch04_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch05_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch05_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch05_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch05_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch05_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch05_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch05_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch05_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch05_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch05_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch05_branchRule", [
			["int", "/TXDrawFractal3D_1/branch05_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch05_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch05_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch05_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch05_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch05_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch05_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch05_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch05_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch05_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch05_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch05_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch05_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch05_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_offsetX", [
			["float", "/TXDrawFractal3D_1/branch05_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch05_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch05_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_offsetY", [
			["float", "/TXDrawFractal3D_1/branch05_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch05_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch05_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch05_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch05_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch05_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_anchorX", [
			["float", "/TXDrawFractal3D_1/branch05_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_anchorY", [
			["float", "/TXDrawFractal3D_1/branch05_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch05_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch05_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch05_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleX", [
			["float", "/TXDrawFractal3D_1/branch05_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch05_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch05_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch05_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleY", [
			["float", "/TXDrawFractal3D_1/branch05_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch05_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch05_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch05_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch05_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch05_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch05_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch05_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch05_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_angle", [
			["float", "/TXDrawFractal3D_1/branch05_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch05_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch05_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch05_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch05_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch05_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch05_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch05_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch05_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch05_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch05_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch05_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch05_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch06_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch06_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch06_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch06_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch06_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch06_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch06_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch06_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch06_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch06_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch06_branchRule", [
			["int", "/TXDrawFractal3D_1/branch06_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch06_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch06_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch06_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch06_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch06_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch06_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch06_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch06_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch06_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch06_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch06_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch06_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch06_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_offsetX", [
			["float", "/TXDrawFractal3D_1/branch06_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch06_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch06_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_offsetY", [
			["float", "/TXDrawFractal3D_1/branch06_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch06_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch06_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch06_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch06_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch06_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_anchorX", [
			["float", "/TXDrawFractal3D_1/branch06_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_anchorY", [
			["float", "/TXDrawFractal3D_1/branch06_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch06_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch06_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch06_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleX", [
			["float", "/TXDrawFractal3D_1/branch06_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch06_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch06_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch06_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleY", [
			["float", "/TXDrawFractal3D_1/branch06_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch06_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch06_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch06_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch06_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch06_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch06_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch06_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch06_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_angle", [
			["float", "/TXDrawFractal3D_1/branch06_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch06_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch06_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch06_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch06_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch06_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch06_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch06_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch06_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch06_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch06_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch06_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch06_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch07_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch07_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch07_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch07_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch07_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch07_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch07_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch07_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch07_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch07_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch07_branchRule", [
			["int", "/TXDrawFractal3D_1/branch07_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch07_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch07_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch07_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch07_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch07_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch07_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch07_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch07_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch07_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch07_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch07_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch07_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch07_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_offsetX", [
			["float", "/TXDrawFractal3D_1/branch07_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch07_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch07_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_offsetY", [
			["float", "/TXDrawFractal3D_1/branch07_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch07_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch07_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch07_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch07_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch07_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_anchorX", [
			["float", "/TXDrawFractal3D_1/branch07_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_anchorY", [
			["float", "/TXDrawFractal3D_1/branch07_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch07_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch07_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch07_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleX", [
			["float", "/TXDrawFractal3D_1/branch07_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch07_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch07_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch07_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleY", [
			["float", "/TXDrawFractal3D_1/branch07_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch07_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch07_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch07_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch07_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch07_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch07_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch07_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch07_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_angle", [
			["float", "/TXDrawFractal3D_1/branch07_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch07_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch07_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch07_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch07_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch07_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch07_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch07_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch07_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch07_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch07_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch07_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch07_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch08_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch08_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch08_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch08_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch08_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch08_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch08_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch08_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch08_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch08_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch08_branchRule", [
			["int", "/TXDrawFractal3D_1/branch08_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch08_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch08_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch08_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch08_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch08_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch08_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch08_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch08_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch08_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch08_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch08_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch08_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch08_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_offsetX", [
			["float", "/TXDrawFractal3D_1/branch08_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch08_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch08_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_offsetY", [
			["float", "/TXDrawFractal3D_1/branch08_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch08_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch08_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch08_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch08_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch08_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_anchorX", [
			["float", "/TXDrawFractal3D_1/branch08_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_anchorY", [
			["float", "/TXDrawFractal3D_1/branch08_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch08_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch08_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch08_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleX", [
			["float", "/TXDrawFractal3D_1/branch08_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch08_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch08_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch08_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleY", [
			["float", "/TXDrawFractal3D_1/branch08_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch08_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch08_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch08_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch08_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch08_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch08_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch08_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch08_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_angle", [
			["float", "/TXDrawFractal3D_1/branch08_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch08_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch08_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch08_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch08_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch08_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch08_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch08_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch08_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch08_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch08_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch08_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch08_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch09_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch09_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch09_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch09_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch09_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch09_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch09_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch09_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch09_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch09_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch09_branchRule", [
			["int", "/TXDrawFractal3D_1/branch09_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch09_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch09_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch09_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch09_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch09_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch09_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch09_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch09_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch09_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch09_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch09_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch09_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch09_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_offsetX", [
			["float", "/TXDrawFractal3D_1/branch09_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch09_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch09_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_offsetY", [
			["float", "/TXDrawFractal3D_1/branch09_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch09_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch09_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch09_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch09_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch09_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_anchorX", [
			["float", "/TXDrawFractal3D_1/branch09_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_anchorY", [
			["float", "/TXDrawFractal3D_1/branch09_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch09_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch09_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch09_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleX", [
			["float", "/TXDrawFractal3D_1/branch09_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch09_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch09_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch09_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleY", [
			["float", "/TXDrawFractal3D_1/branch09_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch09_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch09_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleYLevelDelta/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch09_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch09_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch09_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch09_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch09_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch09_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_angle", [
			["float", "/TXDrawFractal3D_1/branch09_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch09_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch09_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch09_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch09_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch09_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch09_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch09_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch09_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch09_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch09_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch09_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch09_subBranchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch10_branchIfTopLevel", [
			["bool_int", "/TXDrawFractal3D_1/branch10_branchIfTopLevel/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_branchIfTopLevel/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch10_branchIfTopLevel/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_branchIfTopLevel/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch10_drawBottomLevelOnly", [
			["bool_int", "/TXDrawFractal3D_1/branch10_drawBottomLevelOnly/fixedValue", 0],
			["bool_int", "/TXDrawFractal3D_1/branch10_drawBottomLevelOnly/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch10_drawBottomLevelOnly/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_drawBottomLevelOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFractal3D_1/branch10_applyOffsetLast", [
			["bool_int", "/TXDrawFractal3D_1/branch10_applyOffsetLast/fixedValue", 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_applyOffsetLast/fixedModMix", 0],
			["bool_int", "/TXDrawFractal3D_1/branch10_applyOffsetLast/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_applyOffsetLast/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch10_branchRule", [
			["int", "/TXDrawFractal3D_1/branch10_branchRule/fixedValue", 0, 0, 4, ["Always branch", "Branch if Level is Greater than Comparator", "Branch if Level is Less than Comparator",
				"Branch if Level is Equal to Comparator", "Branch if Level is Not Equal to Comparator"]],
			["float", "/TXDrawFractal3D_1/branch10_branchRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_branchRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_branchRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch10_branchRule/softMin", 0, 0, 4],
			["int", "/TXDrawFractal3D_1/branch10_branchRule/softMax", 4, 0, 4],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch10_branchRuleComparator", [
			["int", "/TXDrawFractal3D_1/branch10_branchRuleComparator/fixedValue", 1, 1, 10],
			["float", "/TXDrawFractal3D_1/branch10_branchRuleComparator/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_branchRuleComparator/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_branchRuleComparator/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch10_branchRuleComparator/softMin", 1, 1, 10],
			["int", "/TXDrawFractal3D_1/branch10_branchRuleComparator/softMax", 10, 1, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawFractal3D_1/branch10_drawLayersRule", [
			["int", "/TXDrawFractal3D_1/branch10_drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawFractal3D_1/branch10_drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_drawLayersRule/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawFractal3D_1/branch10_drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawFractal3D_1/branch10_drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_offsetX", [
			["float", "/TXDrawFractal3D_1/branch10_offsetX/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch10_offsetX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_offsetX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_offsetX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_offsetX/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch10_offsetX/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_offsetY", [
			["float", "/TXDrawFractal3D_1/branch10_offsetY/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch10_offsetY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_offsetY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_offsetY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_offsetY/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch10_offsetY/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_offsetZ", [
			["float", "/TXDrawFractal3D_1/branch10_offsetZ/fixedValue", 0, -10, 10],
			["float", "/TXDrawFractal3D_1/branch10_offsetZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_offsetZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_offsetZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_offsetZ/softMin", -10, -10, 10],
			["float", "/TXDrawFractal3D_1/branch10_offsetZ/softMax", 10, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_anchorX", [
			["float", "/TXDrawFractal3D_1/branch10_anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_anchorX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_anchorY", [
			["float", "/TXDrawFractal3D_1/branch10_anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_anchorY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_anchorZ", [
			["float", "/TXDrawFractal3D_1/branch10_anchorZ/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_anchorZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_anchorZ/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawFractal3D_1/branch10_useScaleXForScaleY", 0],
		["bool_int", "/TXDrawFractal3D_1/branch10_useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleX", [
			["float", "/TXDrawFractal3D_1/branch10_scaleX/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch10_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleX/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch10_scaleX/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleXLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleXRandom", [
			["float", "/TXDrawFractal3D_1/branch10_scaleXRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleXRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleXRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleXRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleXRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleXRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleY", [
			["float", "/TXDrawFractal3D_1/branch10_scaleY/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch10_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleY/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch10_scaleY/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleYLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleYRandom", [
			["float", "/TXDrawFractal3D_1/branch10_scaleYRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleYRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleYRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleYRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleYRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleYRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleZ", [
			["float", "/TXDrawFractal3D_1/branch10_scaleZ/fixedValue", 1, 0, 2],
			["float", "/TXDrawFractal3D_1/branch10_scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZ/softMin", 0, 0, 2],
			["float", "/TXDrawFractal3D_1/branch10_scaleZ/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZLevelDelta/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_scaleZRandom", [
			["float", "/TXDrawFractal3D_1/branch10_scaleZRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_scaleZRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_scaleZRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_scaleZRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_rotateAxisX", [
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisX/fixedValue", 1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_rotateAxisX/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisX/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisX/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisX/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_rotateAxisY", [
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisY/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_rotateAxisY/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisY/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisY/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisY/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_rotateAxisZ", [
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_rotateAxisZ/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisZ/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisZ/softMin", -1, -1, 1],
			["float", "/TXDrawFractal3D_1/branch10_rotateAxisZ/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_angle", [
			["float", "/TXDrawFractal3D_1/branch10_angle/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch10_angle/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_angle/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_angle/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_angle/softMin", -180, -720, 720],
			["float", "/TXDrawFractal3D_1/branch10_angle/softMax", 180, -720, 720],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_angleLevelDelta", [
			["float", "/TXDrawFractal3D_1/branch10_angleLevelDelta/fixedValue", 0, -180, 180],
			["float", "/TXDrawFractal3D_1/branch10_angleLevelDelta/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_angleLevelDelta/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_angleLevelDelta/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_angleLevelDelta/softMin", -180, -180, 180],
			["float", "/TXDrawFractal3D_1/branch10_angleLevelDelta/softMax", 180, -180, 180],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_angleRandom", [
			["float", "/TXDrawFractal3D_1/branch10_angleRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_angleRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_angleRandom/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_angleRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_angleRandom/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_angleRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_branchProbability", [
			["float", "/TXDrawFractal3D_1/branch10_branchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_branchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_branchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_branchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_branchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_branchProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFractal3D_1/branch10_subBranchProbability", [
			["float", "/TXDrawFractal3D_1/branch10_subBranchProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_subBranchProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFractal3D_1/branch10_subBranchProbability/useExtMod", 0],
			["float", "/TXDrawFractal3D_1/branch10_subBranchProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_subBranchProbability/softMin", 0, 0, 1],
			["float", "/TXDrawFractal3D_1/branch10_subBranchProbability/softMax", 1, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawFractal3D_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			// ["Global", {arg parameterName;
			// 	var arrNames = ["fractalTreeLevels", "fractalTreeBranches", "cellWidth", "cellHeight", "randomSeed", ];
			// 	arrNames.indexOfEqual(parameterName).notNil;
			// }],
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


