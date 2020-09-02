// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// enum TXSTEPSEQSMOOTHTYPE {TXSTEPSEQSMOOTHTYPE_EXPONENTIAL, TXSTEPSEQSMOOTHTYPE_LINEAR, TXSTEPSEQSMOOTHTYPE_COSINE};
// enum TXSTEPSEQENVTYPE {TXSTEPSEQENVTYPE_GATED, TXSTEPSEQENVTYPE_FIXEDALLOWRETRIG, TXSTEPSEQENVTYPE_FIXEDNORETRIG};
//Gated- envelope is gated by trigger input
//Fixed Length, no retriggering - envelope always completes
//Fixed Length, allow retriggering - envelope always completes

// add text for "smoothType/fixedValue"
//, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]

// add text for "envType/fixedValue"
// , ["Gated - by trigger input", "Fixed length - allow retriggering", "Fixed length - no retriggering"]

TXV_StepSequencer : TXV_Module {
	classvar <defaultName = "TXV Step Seq";
	classvar <>arrInstances;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXStepSequencer_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupInt", "/TXStepSequencer_1/numValidSteps", [
			["int", "/TXStepSequencer_1/numValidSteps/fixedValue", 16, 2, 16],
			["float", "/TXStepSequencer_1/numValidSteps/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/numValidSteps/useExtMod", 0],
			["float", "/TXStepSequencer_1/numValidSteps/extModValue", 0, 0, 1],
			["int", "/TXStepSequencer_1/numValidSteps/softMin", 2, 2, 16],
			["int", "/TXStepSequencer_1/numValidSteps/softMax", 16, 2, 16],
		]],
		[ "modParameterGroupBool", "/TXStepSequencer_1/playLooped", [
			["bool_int", "/TXStepSequencer_1/playLooped/fixedValue", 1],
			["bool_int", "/TXStepSequencer_1/playLooped/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/playLooped/useExtMod", 0],
			["float", "/TXStepSequencer_1/playLooped/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXStepSequencer_1/envType", [
			["int", "/TXStepSequencer_1/envType/fixedValue", 1, 0, 2, ["Gated - by trigger input", "Fixed length - allow retriggering", "Fixed length - no retriggering"]],
			["float", "/TXStepSequencer_1/envType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/envType/useExtMod", 0],
			["float", "/TXStepSequencer_1/envType/extModValue", 0, 0, 1],
			["int", "/TXStepSequencer_1/envType/softMin", 0, 0, 2],
			["int", "/TXStepSequencer_1/envType/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXStepSequencer_1/triggerThreshold", [
			["float", "/TXStepSequencer_1/triggerThreshold/fixedValue", 0.5, 0.001, 0.999],
			["float", "/TXStepSequencer_1/triggerThreshold/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/triggerThreshold/useExtMod", 0],
			["float", "/TXStepSequencer_1/triggerThreshold/extModValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/triggerThreshold/softMin", 0.001, 0.001, 0.999],
			["float", "/TXStepSequencer_1/triggerThreshold/softMax", 0.999, 0.001, 0.999],
		]],
		[ "modParameterGroupFloat", "/TXStepSequencer_1/triggerInput", [
			["float", "/TXStepSequencer_1/triggerInput/fixedValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/triggerInput/fixedModMix", 1, 0, 1],
			["bool_int", "/TXStepSequencer_1/triggerInput/useExtMod", 0],
			["float", "/TXStepSequencer_1/triggerInput/extModValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/triggerInput/softMin", 0, 0, 1],
			["float", "/TXStepSequencer_1/triggerInput/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXStepSequencer_1/releaseTime", [
			["float", "/TXStepSequencer_1/releaseTime/fixedValue", 0, 0, 10],
			["float", "/TXStepSequencer_1/releaseTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/releaseTime/useExtMod", 0],
			["float", "/TXStepSequencer_1/releaseTime/extModValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/releaseTime/softMin", 0, 0, 100],
			["float", "/TXStepSequencer_1/releaseTime/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupBool", "/TXStepSequencer_1/randomiseStepOrder", [
			["bool_int", "/TXStepSequencer_1/randomiseStepOrder/fixedValue", 0],
			["bool_int", "/TXStepSequencer_1/randomiseStepOrder/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/randomiseStepOrder/useExtMod", 0],
			["float", "/TXStepSequencer_1/randomiseStepOrder/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXStepSequencer_1/baselineZero", 1],
		[ "modParameterGroupBool", "/TXStepSequencer_1/frozen", [
			["bool_int", "/TXStepSequencer_1/frozen/fixedValue", 0],
			["bool_int", "/TXStepSequencer_1/frozen/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/frozen/useExtMod", 0],
			["float", "/TXStepSequencer_1/frozen/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXStepSequencer_1/resetClock", [
			["bool_int", "/TXStepSequencer_1/resetClock/fixedValue", 0],
			["bool_int", "/TXStepSequencer_1/resetClock/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/resetClock/useExtMod", 0],
			["float", "/TXStepSequencer_1/resetClock/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXStepSequencer_1/useMasterBpm", [
			["bool_int", "/TXStepSequencer_1/useMasterBpm/fixedValue", 1],
			["bool_int", "/TXStepSequencer_1/useMasterBpm/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/useMasterBpm/useExtMod", 0],
			["float", "/TXStepSequencer_1/useMasterBpm/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXStepSequencer_1/bpm", [
			["float", "/TXStepSequencer_1/bpm/fixedValue", 60, 0.1, 200],
			["float", "/TXStepSequencer_1/bpm/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/bpm/useExtMod", 0],
			["float", "/TXStepSequencer_1/bpm/extModValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/bpm/softMin", 0.1, 0.001, 1000],
			["float", "/TXStepSequencer_1/bpm/softMax", 200, 0.001, 1000],
		]],
		[ "modParameterGroupFloat", "/TXStepSequencer_1/stepTimeBeats", [
			["float", "/TXStepSequencer_1/stepTimeBeats/fixedValue", 1, 1, 64],
			["float", "/TXStepSequencer_1/stepTimeBeats/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/stepTimeBeats/useExtMod", 0],
			["float", "/TXStepSequencer_1/stepTimeBeats/extModValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/stepTimeBeats/softMin", 1, 0.01, 1000],
			["float", "/TXStepSequencer_1/stepTimeBeats/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupFloat", "/TXStepSequencer_1/stepTimeDivisor", [
			["float", "/TXStepSequencer_1/stepTimeDivisor/fixedValue", 1, 1, 64],
			["float", "/TXStepSequencer_1/stepTimeDivisor/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/stepTimeDivisor/useExtMod", 0],
			["float", "/TXStepSequencer_1/stepTimeDivisor/extModValue", 0, 0, 1],
			["float", "/TXStepSequencer_1/stepTimeDivisor/softMin", 1, 0.01, 1000],
			["float", "/TXStepSequencer_1/stepTimeDivisor/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupBool", "/TXStepSequencer_1/useSmoothing", [
			["bool_int", "/TXStepSequencer_1/useSmoothing/fixedValue", 1],
			["bool_int", "/TXStepSequencer_1/useSmoothing/fixedModMix", 0],
			["bool_int", "/TXStepSequencer_1/useSmoothing/useExtMod", 0],
			["float", "/TXStepSequencer_1/useSmoothing/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXStepSequencer_1/smoothingReturnToStep1", 0],
		[ "modParameterGroupInt", "/TXStepSequencer_1/smoothType", [
			["int", "/TXStepSequencer_1/smoothType/fixedValue", 9, 0, 23,
				["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXStepSequencer_1/smoothType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXStepSequencer_1/smoothType/useExtMod", 0],
			["float", "/TXStepSequencer_1/smoothType/extModValue", 0, 0, 1],
			["int", "/TXStepSequencer_1/smoothType/softMin", 0, 0, 23],
			["int", "/TXStepSequencer_1/smoothType/softMax", 23, 0, 23],
		]],
		["float", "/TXStepSequencer_1/step01Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step02Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step03Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step04Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step05Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step06Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step07Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step08Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step09Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step10Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step11Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step12Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step13Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step14Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step15Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step16Value", 0, 0, 1],
		["float", "/TXStepSequencer_1/step01TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step02TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step03TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step04TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step05TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step06TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step07TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step08TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step09TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step10TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step11TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step12TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step13TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step14TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step15TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step16TimeProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step01SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step02SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step03SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step04SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step05SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step06SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step07SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step08SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step09SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step10SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step11SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step12SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step13SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step14SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step15SmoothProportion", 1, 0, 1],
		["float", "/TXStepSequencer_1/step16SmoothProportion", 1, 0, 1],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXStepSequencer_1 ---------- */
		"out",
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Step Values", {arg parameterName;
				parameterName.containsi("Value");
			}],
			["Step Times", {arg parameterName;
				not(parameterName.containsi("releaseTime"))
				&& (
					parameterName.containsi("Time")
					|| parameterName.containsi("bpm"));
			}],
			["Step Smoothing", {arg parameterName;
				parameterName.containsi("Smooth");
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Global";
	}

}


