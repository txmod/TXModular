// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawParticles : TXV_Module {
	classvar <defaultName = "TXV Particles";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maskType/fixedValue":
	// , ["no mask", "use mask image", "feathered square", "circle", "feathered circle", "blurred circle", "ring", "feathered ring", "diamond", "feathered diamond",]

	// add text array to ..."spreadType/fixedValue":
	// , ["Circle", "Horizontal Line", "Vertical Line", "Diagonal Line"]

	// add text for "spreadDistribution/fixedValue" & "triggerDelayDistribution/fixedValue" & "dance_randomSmoothType/fixedValue"
	//, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]

	// add text for "dance_lfoType/fixedValue"
	// , ["No Pattern", "Circles", "Spirals", "Spiral Sine", "Spiral Tri", "Lines", "Diagonal Lines", "Grid", "Sine Wave", "Zig Zag", "Random", "Random Quantised", "Random Smoothed", "Double Random", "Double Random Quantised", "Double Random Smoothed", "Custom Curve", "Custom Curve - Mirrored"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawParticles_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["assetSlot/image", "assetSlot/image/maskImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		["extImageModuleSlot", "extImageModuleSlot/extMaskImageModule", 0],
		[ "modParameterGroupBool", "/TXDrawParticles_1/drawActive", [
			["bool_int", "/TXDrawParticles_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawParticles_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/drawActive/useExtMod", 0],
			["float", "/TXDrawParticles_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawParticles_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawParticles_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawParticles_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXDrawParticles_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawParticles_1/sourceImageScaleMode", [
			["int", "/TXDrawParticles_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXDrawParticles_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXDrawParticles_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXDrawParticles_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		["bool_int", "/TXDrawParticles_1/useExternalSourceImage", 0],
		["bool_int", "/TXDrawParticles_1/useExternalMaskImage", 0],
		[ "modParameterGroupInt", "/TXDrawParticles_1/maskType", [
			["int", "/TXDrawParticles_1/maskType/fixedValue", 0, 0, 9, ["no mask", "use mask image", "feathered square", "circle", "feathered circle", "blurred circle", "ring", "feathered ring", "diamond", "feathered diamond",]],
			["float", "/TXDrawParticles_1/maskType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/maskType/useExtMod", 0],
			["float", "/TXDrawParticles_1/maskType/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/maskType/softMin", 0, 0, 9],
			["int", "/TXDrawParticles_1/maskType/softMax", 9, 0, 9],
		]],
		["bool_int", "/TXDrawParticles_1/invertMask", 0],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/feathering", [
			["float", "/TXDrawParticles_1/feathering/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/feathering/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/feathering/useExtMod", 0],
			["float", "/TXDrawParticles_1/feathering/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/feathering/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/feathering/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/useCentralColourOnly", [
			["bool_int", "/TXDrawParticles_1/useCentralColourOnly/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/useCentralColourOnly/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/useCentralColourOnly/useExtMod", 0],
			["float", "/TXDrawParticles_1/useCentralColourOnly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockColourAtStart", [
			["bool_int", "/TXDrawParticles_1/lockColourAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockColourAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockColourAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockColourAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockAlphaScaleAtStart", [
			["bool_int", "/TXDrawParticles_1/lockAlphaScaleAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockAlphaScaleAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockAlphaScaleAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockAlphaScaleAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockWidthAtStart", [
			["bool_int", "/TXDrawParticles_1/lockWidthAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockWidthAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockWidthAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockWidthAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockHeightAtStart", [
			["bool_int", "/TXDrawParticles_1/lockHeightAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockHeightAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockHeightAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockHeightAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockSpreadAtStart", [
			["bool_int", "/TXDrawParticles_1/lockSpreadAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockSpreadAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockSpreadAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockSpreadAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockScaleAtStart", [
			["bool_int", "/TXDrawParticles_1/lockScaleAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockScaleAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockScaleAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockScaleAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockAnchorAtStart", [
			["bool_int", "/TXDrawParticles_1/lockAnchorAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockAnchorAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockAnchorAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockAnchorAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockRotationOffsetAtStart", [
			["bool_int", "/TXDrawParticles_1/lockRotationOffsetAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockRotationOffsetAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockRotationOffsetAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockRotationOffsetAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockSamplePositionAtStart", [
			["bool_int", "/TXDrawParticles_1/lockSamplePositionAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockSamplePositionAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockSamplePositionAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockSamplePositionAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockDanceWidthHeightAtStart", [
			["bool_int", "/TXDrawParticles_1/lockDanceWidthHeightAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockDanceWidthHeightAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockDanceWidthHeightAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockDanceWidthHeightAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockAttractionPointAtStart", [
			["bool_int", "/TXDrawParticles_1/lockAttractionPointAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockAttractionPointAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockAttractionPointAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockAttractionPointAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/lockTintAtStart", [
			["bool_int", "/TXDrawParticles_1/lockTintAtStart/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/lockTintAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/lockTintAtStart/useExtMod", 0],
			["float", "/TXDrawParticles_1/lockTintAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/triggerContinuosly", [
			["bool_int", "/TXDrawParticles_1/triggerContinuosly/fixedValue", 1],
			["bool_int", "/TXDrawParticles_1/triggerContinuosly/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/triggerContinuosly/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/triggerNow", [
			["bool_int", "/TXDrawParticles_1/triggerNow/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/triggerNow/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/triggerNow/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/killAllNow", [
			["bool_int", "/TXDrawParticles_1/killAllNow/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/killAllNow/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/killAllNow/useExtMod", 0],
			["float", "/TXDrawParticles_1/killAllNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/numParticlesPerTrigger", [
			["int", "/TXDrawParticles_1/numParticlesPerTrigger/fixedValue", 8, 0, 10],
			["float", "/TXDrawParticles_1/numParticlesPerTrigger/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/numParticlesPerTrigger/useExtMod", 0],
			["float", "/TXDrawParticles_1/numParticlesPerTrigger/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/numParticlesPerTrigger/softMin", 0, 0, 100],
			["int", "/TXDrawParticles_1/numParticlesPerTrigger/softMax", 10, 0, 100],
		]],
		["int", "/TXDrawParticles_1/maximumActiveParticles", 200, 2, 200],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bpm", [
			["float", "/TXDrawParticles_1/bpm/fixedValue", 120, 0.1, 200],
			["float", "/TXDrawParticles_1/bpm/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bpm/useExtMod", 0],
			["float", "/TXDrawParticles_1/bpm/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bpm/softMin", 0.1, 0.001, 1000],
			["float", "/TXDrawParticles_1/bpm/softMax", 200, 0.001, 1000],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/useMasterBpm", [
			["bool_int", "/TXDrawParticles_1/useMasterBpm/fixedValue", 1],
			["bool_int", "/TXDrawParticles_1/useMasterBpm/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/useMasterBpm/useExtMod", 0],
			["float", "/TXDrawParticles_1/useMasterBpm/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/stepTimeBeats", [
			["float", "/TXDrawParticles_1/stepTimeBeats/fixedValue", 4, 1, 64],
			["float", "/TXDrawParticles_1/stepTimeBeats/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/stepTimeBeats/useExtMod", 0],
			["float", "/TXDrawParticles_1/stepTimeBeats/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/stepTimeBeats/softMin", 1, 1, 1000],
			["float", "/TXDrawParticles_1/stepTimeBeats/softMax", 64, 1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/stepTimeDivisor", [
			["float", "/TXDrawParticles_1/stepTimeDivisor/fixedValue", 1, 1, 64],
			["float", "/TXDrawParticles_1/stepTimeDivisor/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/stepTimeDivisor/useExtMod", 0],
			["float", "/TXDrawParticles_1/stepTimeDivisor/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/stepTimeDivisor/softMin", 1, 1, 1000],
			["float", "/TXDrawParticles_1/stepTimeDivisor/softMax", 64, 1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/triggerLevel", [
			["float", "/TXDrawParticles_1/triggerLevel/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerLevel/fixedModMix", 1, 0, 1],
			["bool_int", "/TXDrawParticles_1/triggerLevel/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerLevel/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerLevel/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerLevel/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/triggerThreshold", [
			["float", "/TXDrawParticles_1/triggerThreshold/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/triggerThreshold/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/triggerThreshold/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerThreshold/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerThreshold/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerThreshold/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/triggerProbability", [
			["float", "/TXDrawParticles_1/triggerProbability/fixedValue", 1, 0, 1],
			["float", "/TXDrawParticles_1/triggerProbability/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/triggerProbability/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerProbability/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerProbability/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerProbability/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/triggerDelaySpread", [
			["float", "/TXDrawParticles_1/triggerDelaySpread/fixedValue", 1, 0, 1],
			["float", "/TXDrawParticles_1/triggerDelaySpread/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/triggerDelaySpread/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerDelaySpread/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerDelaySpread/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerDelaySpread/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/triggerDelayDistribution", [
			["int", "/TXDrawParticles_1/triggerDelayDistribution/fixedValue", 11, 0, 23, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXDrawParticles_1/triggerDelayDistribution/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/triggerDelayDistribution/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerDelayDistribution/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/triggerDelayDistribution/softMin", 0, 0, 23],
			["int", "/TXDrawParticles_1/triggerDelayDistribution/softMax", 23, 0, 23],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/triggerDelayRandom", [
			["float", "/TXDrawParticles_1/triggerDelayRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerDelayRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/triggerDelayRandom/useExtMod", 0],
			["float", "/TXDrawParticles_1/triggerDelayRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerDelayRandom/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/triggerDelayRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/lifetimeBeats", [
			["float", "/TXDrawParticles_1/lifetimeBeats/fixedValue", 8, 0.01, 32],
			["float", "/TXDrawParticles_1/lifetimeBeats/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/lifetimeBeats/useExtMod", 0],
			["float", "/TXDrawParticles_1/lifetimeBeats/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/lifetimeBeats/softMin", 0.01, 0.01, 128],
			["float", "/TXDrawParticles_1/lifetimeBeats/softMax", 32, 0.01, 128],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/lifetimeRandomVariation", [
			["float", "/TXDrawParticles_1/lifetimeRandomVariation/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/lifetimeRandomVariation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/lifetimeRandomVariation/useExtMod", 0],
			["float", "/TXDrawParticles_1/lifetimeRandomVariation/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/lifetimeRandomVariation/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/lifetimeRandomVariation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/useTriggerTimeForLifetime", [
			["bool_int", "/TXDrawParticles_1/useTriggerTimeForLifetime/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/useTriggerTimeForLifetime/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/useTriggerTimeForLifetime/useExtMod", 0],
			["float", "/TXDrawParticles_1/useTriggerTimeForLifetime/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/spreadType", [
			["int", "/TXDrawParticles_1/spreadType/fixedValue", 0, 0, 3, ["Circle", "Horizontal Line", "Vertical Line", "Diagonal Line"]],
			["float", "/TXDrawParticles_1/spreadType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadType/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadType/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/spreadType/softMin", 0, 0, 3],
			["int", "/TXDrawParticles_1/spreadType/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/spreadDistribution", [
			["int", "/TXDrawParticles_1/spreadDistribution/fixedValue", 9, 0, 23, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXDrawParticles_1/spreadDistribution/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadDistribution/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadDistribution/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/spreadDistribution/softMin", 0, 0, 23],
			["int", "/TXDrawParticles_1/spreadDistribution/softMax", 23, 0, 23],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/spreadCenterX", [
			["float", "/TXDrawParticles_1/spreadCenterX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/spreadCenterX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadCenterX/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadCenterX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadCenterX/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadCenterX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/spreadCenterY", [
			["float", "/TXDrawParticles_1/spreadCenterY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/spreadCenterY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadCenterY/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadCenterY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadCenterY/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadCenterY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/spreadWidth", [
			["float", "/TXDrawParticles_1/spreadWidth/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawParticles_1/spreadWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadWidth/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadWidth/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/spreadHeight", [
			["float", "/TXDrawParticles_1/spreadHeight/fixedValue", 0.3, 0, 1],
			["float", "/TXDrawParticles_1/spreadHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadHeight/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadHeight/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/spreadRotation", [
			["float", "/TXDrawParticles_1/spreadRotation/fixedValue", 0, -360, 360],
			["float", "/TXDrawParticles_1/spreadRotation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadRotation/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadRotation/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadRotation/softMin", -360, -360, 360],
			["float", "/TXDrawParticles_1/spreadRotation/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/spreadRandom", [
			["float", "/TXDrawParticles_1/spreadRandom/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/spreadRandom/useExtMod", 0],
			["float", "/TXDrawParticles_1/spreadRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadRandom/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/spreadRandom/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/posXPerlinMix", [
			["float", "/TXDrawParticles_1/posXPerlinMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/posXPerlinMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/posXPerlinMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/posXPerlinMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/posXPerlinMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/posXPerlinMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/posYPerlinMix", [
			["float", "/TXDrawParticles_1/posYPerlinMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/posYPerlinMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/posYPerlinMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/posYPerlinMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/posYPerlinMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/posYPerlinMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/posXPerlinSpeed", [
			["float", "/TXDrawParticles_1/posXPerlinSpeed/fixedValue", 0.1, 0, 10],
			["float", "/TXDrawParticles_1/posXPerlinSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/posXPerlinSpeed/useExtMod", 0],
			["float", "/TXDrawParticles_1/posXPerlinSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/posXPerlinSpeed/softMin", 0, 0, 100],
			["float", "/TXDrawParticles_1/posXPerlinSpeed/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/posYPerlinSpeed", [
			["float", "/TXDrawParticles_1/posYPerlinSpeed/fixedValue", 0.1, 0, 10],
			["float", "/TXDrawParticles_1/posYPerlinSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/posYPerlinSpeed/useExtMod", 0],
			["float", "/TXDrawParticles_1/posYPerlinSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/posYPerlinSpeed/softMin", 0, 0, 100],
			["float", "/TXDrawParticles_1/posYPerlinSpeed/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/gravityX", [
			["float", "/TXDrawParticles_1/gravityX/fixedValue", 0, -1, 1],
			["float", "/TXDrawParticles_1/gravityX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/gravityX/useExtMod", 0],
			["float", "/TXDrawParticles_1/gravityX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/gravityX/softMin", -1, -10, 10],
			["float", "/TXDrawParticles_1/gravityX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/gravityY", [
			["float", "/TXDrawParticles_1/gravityY/fixedValue", 0.05, -1, 1],
			["float", "/TXDrawParticles_1/gravityY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/gravityY/useExtMod", 0],
			["float", "/TXDrawParticles_1/gravityY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/gravityY/softMin", -1, -10, 10],
			["float", "/TXDrawParticles_1/gravityY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/velocityX", [
			["float", "/TXDrawParticles_1/velocityX/fixedValue", 0, -1, 1],
			["float", "/TXDrawParticles_1/velocityX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/velocityX/useExtMod", 0],
			["float", "/TXDrawParticles_1/velocityX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/velocityX/softMin", -1, -10, 10],
			["float", "/TXDrawParticles_1/velocityX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/velocityY", [
			["float", "/TXDrawParticles_1/velocityY/fixedValue", 0, -1, 1],
			["float", "/TXDrawParticles_1/velocityY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/velocityY/useExtMod", 0],
			["float", "/TXDrawParticles_1/velocityY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/velocityY/softMin", -1, -10, 10],
			["float", "/TXDrawParticles_1/velocityY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/attraction", [
			["float", "/TXDrawParticles_1/attraction/fixedValue", 0, -1, 1],
			["float", "/TXDrawParticles_1/attraction/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/attraction/useExtMod", 0],
			["float", "/TXDrawParticles_1/attraction/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/attraction/softMin", -1, -10, 10],
			["float", "/TXDrawParticles_1/attraction/softMax", 1, -10, 10],
		]],
		["float256", "/TXDrawParticles_1/attractionCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/attractionPointX", [
			["float", "/TXDrawParticles_1/attractionPointX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/attractionPointX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/attractionPointX/useExtMod", 0],
			["float", "/TXDrawParticles_1/attractionPointX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/attractionPointX/softMin", 0, -1, 2],
			["float", "/TXDrawParticles_1/attractionPointX/softMax", 1, -1, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/attractionPointY", [
			["float", "/TXDrawParticles_1/attractionPointY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/attractionPointY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/attractionPointY/useExtMod", 0],
			["float", "/TXDrawParticles_1/attractionPointY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/attractionPointY/softMin", 0, -1, 2],
			["float", "/TXDrawParticles_1/attractionPointY/softMax", 1, -1, 2],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/useWidthForHeight", [
			["bool_int", "/TXDrawParticles_1/useWidthForHeight/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/useWidthForHeight/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/useWidthForHeight/useExtMod", 0],
			["float", "/TXDrawParticles_1/useWidthForHeight/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/width", [
			["float", "/TXDrawParticles_1/width/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawParticles_1/width/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/width/useExtMod", 0],
			["float", "/TXDrawParticles_1/width/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/width/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/width/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawParticles_1/widthCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/widthPerlinMix", [
			["float", "/TXDrawParticles_1/widthPerlinMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/widthPerlinMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/widthPerlinMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/widthPerlinMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/widthPerlinMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/widthPerlinMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/widthPerlinSpeed", [
			["float", "/TXDrawParticles_1/widthPerlinSpeed/fixedValue", 0.1, 0, 10],
			["float", "/TXDrawParticles_1/widthPerlinSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/widthPerlinSpeed/useExtMod", 0],
			["float", "/TXDrawParticles_1/widthPerlinSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/widthPerlinSpeed/softMin", 0, 0, 100],
			["float", "/TXDrawParticles_1/widthPerlinSpeed/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/height", [
			["float", "/TXDrawParticles_1/height/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawParticles_1/height/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/height/useExtMod", 0],
			["float", "/TXDrawParticles_1/height/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/height/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/height/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawParticles_1/heightCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/heightPerlinMix", [
			["float", "/TXDrawParticles_1/heightPerlinMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/heightPerlinMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/heightPerlinMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/heightPerlinMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/heightPerlinMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/heightPerlinMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/heightPerlinSpeed", [
			["float", "/TXDrawParticles_1/heightPerlinSpeed/fixedValue", 0.1, 0, 10],
			["float", "/TXDrawParticles_1/heightPerlinSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/heightPerlinSpeed/useExtMod", 0],
			["float", "/TXDrawParticles_1/heightPerlinSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/heightPerlinSpeed/softMin", 0, 0, 100],
			["float", "/TXDrawParticles_1/heightPerlinSpeed/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/useScaleXForScaleY", [
			["bool_int", "/TXDrawParticles_1/useScaleXForScaleY/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/useScaleXForScaleY/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/useScaleXForScaleY/useExtMod", 0],
			["float", "/TXDrawParticles_1/useScaleXForScaleY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/scaleX", [
			["float", "/TXDrawParticles_1/scaleX/fixedValue", 0.5, 0, 2],
			["float", "/TXDrawParticles_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/scaleX/useExtMod", 0],
			["float", "/TXDrawParticles_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/scaleX/softMin", 0, 0, 10],
			["float", "/TXDrawParticles_1/scaleX/softMax", 2, 0, 10],
		]],
		["float256", "/TXDrawParticles_1/scaleXCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/scaleY", [
			["float", "/TXDrawParticles_1/scaleY/fixedValue", 0.5, 0, 2],
			["float", "/TXDrawParticles_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/scaleY/useExtMod", 0],
			["float", "/TXDrawParticles_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/scaleY/softMin", 0, 0, 10],
			["float", "/TXDrawParticles_1/scaleY/softMax", 2, 0, 10],
		]],
		["float256", "/TXDrawParticles_1/scaleYCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/alphaScale", [
			["float", "/TXDrawParticles_1/alphaScale/fixedValue", 1, 0, 1],
			["float", "/TXDrawParticles_1/alphaScale/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/alphaScale/useExtMod", 0],
			["float", "/TXDrawParticles_1/alphaScale/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/alphaScale/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/alphaScale/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawParticles_1/alphaCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/alphaPerlinMix", [
			["float", "/TXDrawParticles_1/alphaPerlinMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/alphaPerlinMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/alphaPerlinMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/alphaPerlinMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/alphaPerlinMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/alphaPerlinMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/alphaPerlinSpeed", [
			["float", "/TXDrawParticles_1/alphaPerlinSpeed/fixedValue", 0.1, 0, 10],
			["float", "/TXDrawParticles_1/alphaPerlinSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/alphaPerlinSpeed/useExtMod", 0],
			["float", "/TXDrawParticles_1/alphaPerlinSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/alphaPerlinSpeed/softMin", 0, 0, 100],
			["float", "/TXDrawParticles_1/alphaPerlinSpeed/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/tintMix", [
			["float", "/TXDrawParticles_1/tintMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/tintMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/tintMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/tintHue", [
			["float", "/TXDrawParticles_1/tintHue/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/tintHue/useExtMod", 0],
			["float", "/TXDrawParticles_1/tintHue/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintHue/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/tintHueRotate", [
			["float", "/TXDrawParticles_1/tintHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/tintHueRotate/useExtMod", 0],
			["float", "/TXDrawParticles_1/tintHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/tintSaturation", [
			["float", "/TXDrawParticles_1/tintSaturation/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/tintSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/tintSaturation/useExtMod", 0],
			["float", "/TXDrawParticles_1/tintSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/tintBrightness", [
			["float", "/TXDrawParticles_1/tintBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/tintBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/tintBrightness/useExtMod", 0],
			["float", "/TXDrawParticles_1/tintBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/tintBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/tintHSBAsRGB", [
			["bool_int", "/TXDrawParticles_1/tintHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/tintHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/tintHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawParticles_1/tintHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/anchorX", [
			["float", "/TXDrawParticles_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/anchorX/useExtMod", 0],
			["float", "/TXDrawParticles_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/anchorX/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawParticles_1/anchorXCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/anchorY", [
			["float", "/TXDrawParticles_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/anchorY/useExtMod", 0],
			["float", "/TXDrawParticles_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/anchorY/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawParticles_1/anchorYCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/rotationOffset", [
			["float", "/TXDrawParticles_1/rotationOffset/fixedValue", 0, -360, 360],
			["float", "/TXDrawParticles_1/rotationOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/rotationOffset/useExtMod", 0],
			["float", "/TXDrawParticles_1/rotationOffset/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/rotationOffset/softMin", -360, -360, 360],
			["float", "/TXDrawParticles_1/rotationOffset/softMax", 360, -360, 360],
		]],
		["float256", "/TXDrawParticles_1/rotationCurve", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/rotationPerlinMix", [
			["float", "/TXDrawParticles_1/rotationPerlinMix/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/rotationPerlinMix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/rotationPerlinMix/useExtMod", 0],
			["float", "/TXDrawParticles_1/rotationPerlinMix/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/rotationPerlinMix/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/rotationPerlinMix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/rotationPerlinSpeed", [
			["float", "/TXDrawParticles_1/rotationPerlinSpeed/fixedValue", 0.1, 0, 10],
			["float", "/TXDrawParticles_1/rotationPerlinSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/rotationPerlinSpeed/useExtMod", 0],
			["float", "/TXDrawParticles_1/rotationPerlinSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/rotationPerlinSpeed/softMin", 0, 0, 100],
			["float", "/TXDrawParticles_1/rotationPerlinSpeed/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/useParticlePosForSamplePos", [
			["bool_int", "/TXDrawParticles_1/useParticlePosForSamplePos/fixedValue", 1],
			["bool_int", "/TXDrawParticles_1/useParticlePosForSamplePos/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/useParticlePosForSamplePos/useExtMod", 0],
			["float", "/TXDrawParticles_1/useParticlePosForSamplePos/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/samplePosX", [
			["float", "/TXDrawParticles_1/samplePosX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/samplePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/samplePosX/useExtMod", 0],
			["float", "/TXDrawParticles_1/samplePosX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/samplePosX/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/samplePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/samplePosY", [
			["float", "/TXDrawParticles_1/samplePosY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawParticles_1/samplePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/samplePosY/useExtMod", 0],
			["float", "/TXDrawParticles_1/samplePosY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/samplePosY/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/samplePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/wavesHorizontalOn", [
			["bool_int", "/TXDrawParticles_1/wavesHorizontalOn/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/wavesHorizontalOn/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/wavesHorizontalOn/useExtMod", 0],
			["float", "/TXDrawParticles_1/wavesHorizontalOn/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/wavesVerticalOn", [
			["bool_int", "/TXDrawParticles_1/wavesVerticalOn/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/wavesVerticalOn/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/wavesVerticalOn/useExtMod", 0],
			["float", "/TXDrawParticles_1/wavesVerticalOn/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/wavesQuantised", [
			["bool_int", "/TXDrawParticles_1/wavesQuantised/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/wavesQuantised/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/wavesQuantised/useExtMod", 0],
			["float", "/TXDrawParticles_1/wavesQuantised/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/numWavesX", [
			["float", "/TXDrawParticles_1/numWavesX/fixedValue", 1, 0, 5],
			["float", "/TXDrawParticles_1/numWavesX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/numWavesX/useExtMod", 0],
			["float", "/TXDrawParticles_1/numWavesX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/numWavesX/softMin", 0, 0, 20],
			["float", "/TXDrawParticles_1/numWavesX/softMax", 5, 0, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/numWavesY", [
			["float", "/TXDrawParticles_1/numWavesY/fixedValue", 1, 0, 5],
			["float", "/TXDrawParticles_1/numWavesY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/numWavesY/useExtMod", 0],
			["float", "/TXDrawParticles_1/numWavesY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/numWavesY/softMin", 0, 0, 20],
			["float", "/TXDrawParticles_1/numWavesY/softMax", 5, 0, 20],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/bendOn", [
			["bool_int", "/TXDrawParticles_1/bendOn/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/bendOn/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/bendOn/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendOn/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendRandomPhaseX", [
			["float", "/TXDrawParticles_1/bendRandomPhaseX/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendRandomPhaseX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendRandomPhaseX/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendRandomPhaseX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendRandomPhaseX/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendRandomPhaseX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendRandomPhaseY", [
			["float", "/TXDrawParticles_1/bendRandomPhaseY/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendRandomPhaseY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendRandomPhaseY/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendRandomPhaseY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendRandomPhaseY/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendRandomPhaseY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendPhaseX", [
			["float", "/TXDrawParticles_1/bendPhaseX/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendPhaseX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendPhaseX/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendPhaseX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendPhaseX/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendPhaseX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendPhaseY", [
			["float", "/TXDrawParticles_1/bendPhaseY/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendPhaseY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendPhaseY/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendPhaseY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendPhaseY/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendPhaseY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendDepthX", [
			["float", "/TXDrawParticles_1/bendDepthX/fixedValue", 0.2, 0, 4],
			["float", "/TXDrawParticles_1/bendDepthX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendDepthX/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendDepthX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendDepthX/softMin", 0, 0, 20],
			["float", "/TXDrawParticles_1/bendDepthX/softMax", 4, 0, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendDepthY", [
			["float", "/TXDrawParticles_1/bendDepthY/fixedValue", 0.2, 0, 4],
			["float", "/TXDrawParticles_1/bendDepthY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendDepthY/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendDepthY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendDepthY/softMin", 0, 0, 20],
			["float", "/TXDrawParticles_1/bendDepthY/softMax", 4, 0, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/bendZoom", [
			["float", "/TXDrawParticles_1/bendZoom/fixedValue", 1, 0, 10],
			["float", "/TXDrawParticles_1/bendZoom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/bendZoom/useExtMod", 0],
			["float", "/TXDrawParticles_1/bendZoom/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/bendZoom/softMin", 0, 0, 10],
			["float", "/TXDrawParticles_1/bendZoom/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_width", [
			["float", "/TXDrawParticles_1/dance_width/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_width/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_width/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_width/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_width/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_width/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_height", [
			["float", "/TXDrawParticles_1/dance_height/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_height/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_height/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_height/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_height/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_height/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/dance_lfoType", [
			["int", "/TXDrawParticles_1/dance_lfoType/fixedValue", 1, 0, 17, ["No Pattern", "Circles", "Spirals", "Spiral Sine", "Spiral Tri", "Lines", "Diagonal Lines", "Grid", "Sine Wave", "Zig Zag", "Random", "Random Quantised", "Random Smoothed", "Double Random", "Double Random Quantised", "Double Random Smoothed", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXDrawParticles_1/dance_lfoType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_lfoType/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_lfoType/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/dance_lfoType/softMin", 0, 0, 17],
			["int", "/TXDrawParticles_1/dance_lfoType/softMax", 17, 0, 17],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/dance_randomSmoothType", [
			["int", "/TXDrawParticles_1/dance_randomSmoothType/fixedValue", 11, 0, 23, ["Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXDrawParticles_1/dance_randomSmoothType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_randomSmoothType/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_randomSmoothType/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/dance_randomSmoothType/softMin", 0, 0, 23],
			["int", "/TXDrawParticles_1/dance_randomSmoothType/softMax", 23, 0, 23],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_frozen", [
			["bool_int", "/TXDrawParticles_1/dance_frozen/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/dance_frozen/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_frozen/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_frozen/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawParticles_1/dance_noLevels", [
			["int", "/TXDrawParticles_1/dance_noLevels/fixedValue", 1, 1, 20],
			["float", "/TXDrawParticles_1/dance_noLevels/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_noLevels/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_noLevels/extModValue", 0, 0, 1],
			["int", "/TXDrawParticles_1/dance_noLevels/softMin", 1, 1, 20],
			["int", "/TXDrawParticles_1/dance_noLevels/softMax", 20, 1, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_bpm", [
			["float", "/TXDrawParticles_1/dance_bpm/fixedValue", 120, 0.1, 200],
			["float", "/TXDrawParticles_1/dance_bpm/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_bpm/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_bpm/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_bpm/softMin", 0.1, 0.001, 1000],
			["float", "/TXDrawParticles_1/dance_bpm/softMax", 200, 0.001, 1000],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_useMasterBpm", [
			["bool_int", "/TXDrawParticles_1/dance_useMasterBpm/fixedValue", 1],
			["bool_int", "/TXDrawParticles_1/dance_useMasterBpm/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_useMasterBpm/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_useMasterBpm/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_cycleTimeBeats", [
			["float", "/TXDrawParticles_1/dance_cycleTimeBeats/fixedValue", 4, 1, 64],
			["float", "/TXDrawParticles_1/dance_cycleTimeBeats/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_cycleTimeBeats/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_cycleTimeBeats/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_cycleTimeBeats/softMin", 1, 1, 1000],
			["float", "/TXDrawParticles_1/dance_cycleTimeBeats/softMax", 64, 1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_cycleTimeDivisor", [
			["float", "/TXDrawParticles_1/dance_cycleTimeDivisor/fixedValue", 1, 1, 64],
			["float", "/TXDrawParticles_1/dance_cycleTimeDivisor/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_cycleTimeDivisor/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_cycleTimeDivisor/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_cycleTimeDivisor/softMin", 1, 1, 1000],
			["float", "/TXDrawParticles_1/dance_cycleTimeDivisor/softMax", 64, 1, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_phaseOffset", [
			["float", "/TXDrawParticles_1/dance_phaseOffset/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_phaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_phaseOffset/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_phaseOffset/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_phaseOffset/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_phaseOffset/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_circularDirectionOut", [
			["bool_int", "/TXDrawParticles_1/dance_circularDirectionOut/fixedValue", 1],
			["bool_int", "/TXDrawParticles_1/dance_circularDirectionOut/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_circularDirectionOut/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_circularDirectionOut/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_reverseX", [
			["bool_int", "/TXDrawParticles_1/dance_reverseX/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/dance_reverseX/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_reverseX/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_reverseX/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_reverseY", [
			["bool_int", "/TXDrawParticles_1/dance_reverseY/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/dance_reverseY/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_reverseY/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_reverseY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_swapXAndY", [
			["bool_int", "/TXDrawParticles_1/dance_swapXAndY/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/dance_swapXAndY/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_swapXAndY/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_swapXAndY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_scaleX", [
			["float", "/TXDrawParticles_1/dance_scaleX/fixedValue", 1, 0, 1],
			["float", "/TXDrawParticles_1/dance_scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_scaleX/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_scaleX/softMin", 0, 1, 1],
			["float", "/TXDrawParticles_1/dance_scaleX/softMax", 1, 1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_scaleY", [
			["float", "/TXDrawParticles_1/dance_scaleY/fixedValue", 1, 0, 1],
			["float", "/TXDrawParticles_1/dance_scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_scaleY/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_scaleY/softMin", 0, 1, 1],
			["float", "/TXDrawParticles_1/dance_scaleY/softMax", 1, 1, 1],
		]],
		["float256", "/TXDrawParticles_1/dance_customCurveX", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawParticles_1/dance_customCurveX2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_customCurveXMorph", [
			["float", "/TXDrawParticles_1/dance_customCurveXMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_customCurveXMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_customCurveXMorph/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_customCurveXMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_customCurveXMorph/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_customCurveXMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawParticles_1/dance_customCurveY", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawParticles_1/dance_customCurveY2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawParticles_1/dance_customCurveYMorph", [
			["float", "/TXDrawParticles_1/dance_customCurveYMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_customCurveYMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawParticles_1/dance_customCurveYMorph/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_customCurveYMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_customCurveYMorph/softMin", 0, 0, 1],
			["float", "/TXDrawParticles_1/dance_customCurveYMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawParticles_1/dance_randomStartPhase", [
			["bool_int", "/TXDrawParticles_1/dance_randomStartPhase/fixedValue", 0],
			["bool_int", "/TXDrawParticles_1/dance_randomStartPhase/fixedModMix", 0],
			["bool_int", "/TXDrawParticles_1/dance_randomStartPhase/useExtMod", 0],
			["float", "/TXDrawParticles_1/dance_randomStartPhase/extModValue", 0, 0, 1],
		]],

	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawParticles_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		var trigNames = ["triggerContinuosly", "triggerNow", "killAllNow", "numParticlesPerTrigger",
			"maximumActiveParticles", "triggerLevel", "triggerThreshold", "triggerProbability",];
		var trigDelayNames = ["triggerDelaySpread", "triggerDelayDistribution", "triggerDelayRandom", ];
		var trigBpmTimeNames = ["bpm", "useMasterBpm", "stepTimeBeats", "stepTimeDivisor", "lifetimeBeats",
			"lifetimeRandomVariation", "useTriggerTimeForLifetime", ];
		var imageMaskNames = [ "sourceImageAsset", "maskImageAsset", "extSourceImageModule", "extMaskImageModule",
			"sourceImageScaleMode", "useExternalSourceImage", "useExternalMaskImage", "maskType", "invertMask", "feathering"];
		var maxSizeNames = ["maxWidthHeightRule", "customMaxWidth", "customMaxHeight", "customMaxDepth", ];
		var widthNames = ["width", "widthCurve", "widthPerlinMix", "widthPerlinSpeed", ];
		var heightNames = ["useWidthForHeight", "height", "heightCurve", "heightPerlinMix", "heightPerlinSpeed", ];
		var sampleNames = ["useParticlePosForSamplePos", "samplePosX", "samplePosY", "useCentralColourOnly",];
		var alphaNames = ["alphaScale", "alphaCurve", "alphaPerlinMix", "alphaPerlinSpeed", ];
		var bendNames = ["bendOn", "bendRandomPhaseX", "bendRandomPhaseY", "bendPhaseX", "bendPhaseY", "bendDepthX",
			"bendDepthY", "bendZoom", ];
		var wavesNames = ["wavesHorizontalOn", "wavesVerticalOn", "wavesQuantised", "numWavesX", "numWavesY", ];
		var perlinNames = ["posXPerlinMix", "posYPerlinMix", "posXPerlinSpeed", "posYPerlinSpeed", ];
		var gravityVelocityNames = ["gravityX", "gravityY", "velocityX", "velocityY", ];
		var attractionNames = ["attraction", "attractionCurve", "attractionPointX", "attractionPointY", ];
		var danceTypeOptionsNames = ["dance_lfoType", "dance_randomSmoothType", "dance_frozen", "dance_noLevels",
			"dance_phaseOffset", "dance_circularDirectionOut", "dance_reverseX", "dance_reverseY", "dance_swapXAndY", "dance_randomStartPhase" ];
		var danceScaleNames = ["dance_width", "dance_height", "dance_scaleX", "dance_scaleY",  ];
		var danceCycleNames = ["dance_bpm", "dance_useMasterBpm", "dance_cycleTimeBeats", "dance_cycleTimeDivisor", ];
		var danceCurveNames = ["dance_customCurveX", "dance_customCurveX2", "dance_customCurveXMorph",
			"dance_customCurveY", "dance_customCurveY2", "dance_customCurveYMorph",];
		var activeSpreadNames = ["drawActive", "spreadType", "spreadDistribution", "spreadCenterX", "spreadCenterY",
			"spreadWidth", "spreadHeight", "spreadRotation", "spreadRandom", ];
		var scaleNames = ["useScaleXForScaleY", "scaleX", "scaleXCurve", "scaleY", "scaleYCurve", ];
		var anchorNames = ["anchorX", "anchorXCurve", "anchorY", "anchorYCurve", ];
		var rotationNames = ["rotationOffset", "rotationCurve", "rotationPerlinMix", "rotationPerlinSpeed", ];
		^[
			// format: ["Example Name", nameValidityFunction],
			["Triggering$Options",{arg argName; trigNames.indexOfEqual(argName).notNil;}],
			["Triggering$Trig Delay",{arg argName; trigDelayNames.indexOfEqual(argName).notNil;}],
			["Triggering$BPM, Step Time, Lifetime",{arg argName; trigBpmTimeNames.indexOfEqual(argName).notNil;}],

			["Locks", {arg parameterName; parameterName.containsi("lock");}],

			["Brush$Image & Mask", {arg argName; imageMaskNames.indexOfEqual(argName).notNil;}],
			["Brush$Max Size", {arg argName; maxSizeNames.indexOfEqual(argName).notNil;}],
			["Brush$Width", {arg argName; widthNames.indexOfEqual(argName).notNil;}],
			["Brush$Height", {arg argName; heightNames.indexOfEqual(argName).notNil;}],
			["Brush$Sample", {arg argName; sampleNames.indexOfEqual(argName).notNil;}],
			["Brush$Tint", {arg argName; argName.containsi("Tint");}],
			["Brush$Alpha", {arg argName; alphaNames.indexOfEqual(argName).notNil;}],
			["Brush$Bend", {arg argName; bendNames.indexOfEqual(argName).notNil;}],
			["Brush$Waves", {arg argName; wavesNames.indexOfEqual(argName).notNil;}],

			["Movement$Perlin",{arg argName; perlinNames.indexOfEqual(argName).notNil;}],
			["Movement$Gravity & Velocity",{arg argName; gravityVelocityNames.indexOfEqual(argName).notNil;}],
			["Movement$Attraction",{arg argName; attractionNames.indexOfEqual(argName).notNil;}],
			["Movement$Dance Options",{arg argName; danceTypeOptionsNames.indexOfEqual(argName).notNil;}],
			["Movement$Dance Dimensions",{arg argName; danceScaleNames.indexOfEqual(argName).notNil;}],
			["Movement$Dance Cycle Time",{arg argName; danceCycleNames.indexOfEqual(argName).notNil;}],
			["Movement$Dance Curves",{arg argName; danceCurveNames.indexOfEqual(argName).notNil;}],

			["Draw$Active & Spread",{arg argName; activeSpreadNames.indexOfEqual(argName).notNil;}],
			["Draw$Scale",{arg argName; scaleNames.indexOfEqual(argName).notNil;}],
			["Draw$Anchor",{arg argName; anchorNames.indexOfEqual(argName).notNil;}],
			["Draw$Rotation",{arg argName; rotationNames.indexOfEqual(argName).notNil;}],
		];
	}

	// override
	getInitialDisplayOption {
		^"Triggering$Options";
	}

	initExtraActions { // override
		arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
	}
	extraSubModuleData { // override
		^[arrSlotData];
	}
	loadExtraSubModuleData {arg argData;  // override
		arrSlotData = argData[0];
	}

	getExtraSynthArgSpecs { //  override
		^[
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
	}

}


