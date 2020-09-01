// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// add text for "perlinType/fixedValue"
// , ["Normal", "Quantised"]

TXV_Perlin : TXV_Module {
	classvar <defaultName = "TXV Perlin";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXPerlin_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupInt", "/TXPerlin_1/perlinType", [
			["int", "/TXPerlin_1/perlinType/fixedValue", 0, 0, 1 , ["Normal", "Quantised"]],
			["float", "/TXPerlin_1/perlinType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXPerlin_1/perlinType/useExtMod", 0],
			["float", "/TXPerlin_1/perlinType/extModValue", 0, 0, 1],
			["int", "/TXPerlin_1/perlinType/softMin", 0, 0, 1],
			["int", "/TXPerlin_1/perlinType/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXPerlin_1/speed", [
			["float", "/TXPerlin_1/speed/fixedValue", 1, 0.01, 20],
			["float", "/TXPerlin_1/speed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXPerlin_1/speed/useExtMod", 0],
			["float", "/TXPerlin_1/speed/extModValue", 0, 0, 1],
			["float", "/TXPerlin_1/speed/softMin", 0.01, 0.001, 100.0],
			["float", "/TXPerlin_1/speed/softMax", 20, 0.001, 100.0],
		]],
		[ "modParameterGroupBool", "/TXPerlin_1/useMasterBpm", [
			["bool_int", "/TXPerlin_1/useMasterBpm/fixedValue", 1],
			["bool_int", "/TXPerlin_1/useMasterBpm/fixedModMix", 0],
			["bool_int", "/TXPerlin_1/useMasterBpm/useExtMod", 0],
			["float", "/TXPerlin_1/useMasterBpm/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXPerlin_1/speedDivisor", [
			["float", "/TXPerlin_1/speedDivisor/fixedValue", 1, 1, 64],
			["float", "/TXPerlin_1/speedDivisor/fixedModMix", 0, 0, 1],
			["bool_int", "/TXPerlin_1/speedDivisor/useExtMod", 0],
			["float", "/TXPerlin_1/speedDivisor/extModValue", 0, 0, 1],
			["float", "/TXPerlin_1/speedDivisor/softMin", 1, 0.01, 1000],
			["float", "/TXPerlin_1/speedDivisor/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupBool", "/TXPerlin_1/frozen", [
			["bool_int", "/TXPerlin_1/frozen/fixedValue", 0],
			["bool_int", "/TXPerlin_1/frozen/fixedModMix", 0],
			["bool_int", "/TXPerlin_1/frozen/useExtMod", 0],
			["float", "/TXPerlin_1/frozen/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXPerlin_1 ---------- */
		"out",
	];}
}


