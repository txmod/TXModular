// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXAudioOptionsScreen {		// Information Screen module

	classvar system, w;

	*makeWindow{ arg argSystem, holdServerOptions, argConfirmFunction;
		var txtTitle, btnHelp, popAudioDevice, popBufferSize, popSampleRate;
		var btnConfirm, btnCancel;
		var arrRejectStrings, arrValidDevices;

		arrRejectStrings = ["Built-in Microphone", "Built-in Input", "Built-in Output"];
		system = argSystem;
		w = Window("TX Modular System", Rect(0, 600, 720, 460));
		w.front;
		w.onClose = {this.close};
		w.view.decorator = FlowLayout(w.view.bounds);
		w.view.background = TXColor.sysMainWindow;
		w.view.decorator.shift(36, 36);

		// system title
		txtTitle = StaticText(w, 270 @ 30)
		.string_("TX Modular - Audio Setup")
		.font_(Font.new("Helvetica-Bold",14))
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);


		// decorator
		w.view.decorator.shift(30, 0);
		// button - help
		btnHelp = Button(w, 60 @ 30);
		btnHelp.states = [["Help", TXColor.white, TXColor.sysHelpCol]];
		btnHelp.action = {
			// TXHelpScreen.open;
			TXHelpScreen.openFile("TX_2 Audio Setup");
		};

		// decorator
		w.view.decorator.shift(30, 0);
		// confirm button
		btnConfirm = Button(w, 140 @ 30)
		.states = [["Start Audio Engine", TXColor.white, TXColor.sysGuiCol1]];
		btnConfirm.action = {
			w.onClose = nil;
			if (w.isClosed.not, {w.close});
			// run confirm function
			argConfirmFunction.value;
		};
		btnConfirm.focus(true);

		// decorator
		w.view.decorator.shift(30, 0);
		// quit button
		btnCancel = Button(w, 60 @ 30)
		.states = [["Quit", TXColor.white, TXColor.sysDeleteCol]];
		btnCancel.action = {
			system.dataBank.audioSetupQuit = true;
			this.close;
		};

		// decorator
		w.view.decorator.nextLine;
		w.view.decorator.shift(36, 50);

		// Title
		StaticText(w, 270 @ 27)
		.string_("Audio Device for Input and Output")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// decorator
		w.view.decorator.shift(30, 0);

		// only show popup if platform is OSX
		if (thisProcess.platform.name == 'osx', {
			// popup - Audio device
			arrValidDevices = ServerOptions.devices.reject({ |item, i|
				arrRejectStrings.indexOfEqual(item).notNil
			});
			popAudioDevice = PopUpMenu(w, 230 @ 27)
			.background_(TXColor.sysGuiCol2).stringColor_(TXColor.white);
			popAudioDevice.items = ["Use Default Device"]
			++ arrValidDevices.collect({ |item, i|
				item.replace("(", "[").replace(")", "]");
			});
			popAudioDevice.action = {|view|
				var arrDevices;
				arrDevices = [nil] ++ arrValidDevices;
				system.dataBank.audioDevice = arrDevices.at(view.value);
				holdServerOptions.device = system.dataBank.audioDevice;
				system.saveSystemSettings;
			};
			popAudioDevice.value = ([nil] ++ ServerOptions.devices).indexOfEqual(system.dataBank.audioDevice) ? 0;
		});

		// decorator
		w.view.decorator.nextLine;
		w.view.decorator.shift(36, 40);

		// popup - sample rate
		StaticText(w, 270 @ 27)
		.string_("Audio Device Sample Rate ")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// decorator
		w.view.decorator.shift(30, 0);

		popSampleRate = PopUpMenu(w, 230 @ 27)
		.background_(TXColor.sysGuiCol2).stringColor_(TXColor.white);
		popSampleRate.items = ["Use Default Sample Rate", "8000", "11025", "22050", "32000", "44100",
			"48000", "88200", "96000", "176400", "192000" ];
		popSampleRate.action = {|view|
			var arrSampleRates;
			arrSampleRates = [nil, 8000, 11025, 22050, 32000, 44100, 48000,
				88200, 96000, 176400, 192000] ;
			system.dataBank.sampleRate = arrSampleRates.at(view.value);
			holdServerOptions.sampleRate = system.dataBank.sampleRate;
			system.saveSystemSettings;
		};
		popSampleRate.value = [nil, 8000, 11025, 22050, 32000, 44100, 48000,
			88200, 96000, 176400, 192000]
		.indexOfEqual(system.dataBank.sampleRate) ? 0;

		// decorator
		w.view.decorator.nextLine;
		w.view.decorator.shift(36, 40);

		StaticText(w, 270 @ 27)
		.string_("Audio Device Buffer Size ")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// decorator
		w.view.decorator.shift(30, 0);

		// popup - Buffer size
		popBufferSize = PopUpMenu(w, 230 @ 27)
		.background_(TXColor.sysGuiCol2).stringColor_(TXColor.white);
		popBufferSize.items = ["Use Default Buffer Size","2048", "1024", "512", "256", "128", "64", "32"];
		popBufferSize.action = {|view|
			var arrBufferSizes;
			arrBufferSizes = [nil, 2048, 1024, 512, 256, 128, 64, 32] ;
			system.dataBank.bufferSize = arrBufferSizes.at(view.value);
			holdServerOptions.hardwareBufferSize = system.dataBank.bufferSize;
			system.saveSystemSettings;
		};
		popBufferSize.value =
		[nil, 2048, 1024, 512, 256, 128, 64, 32].indexOfEqual(system.dataBank.bufferSize) ? 0;

		// decorator
		w.view.decorator.nextLine;
		w.view.decorator.shift(36, 36);

		// Title
		StaticText(w, 630 @ 80)
		.font_(Font("Arial", 12))
		.background_(TXColor.sysGuiCol4)
		.stringColor_(TXColor.white)
		.string_(

			" NOTE: Not all sample rates and buffer sizes will work on all audio devices.
 See hardware documentation (or open Audio MIDI Setup on MacOSX) to check which settings can be used.
 If you have problems starting the audio, choose 'Use Default ...' for all options."
		);
	}

	*close {		//	close window
		// if standalone system then quit completely else just close TX
		if (system.txStandAlone == 1, {
			TXSystem1.quitStandalone;
		},{
			if (w.isClosed.not, {w.close});
		});
	}

}
