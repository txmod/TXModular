// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOpenFileScreen {
	classvar w, <defaultSize;

	*initClass {
		defaultSize = 810 @ 350;
	}

	*new{ arg system, arrFileNames, winColour, inLeft=10, inTop=1000;
		this.makeWindow(system, arrFileNames, winColour, inLeft, inTop);
	}

	*makeWindow{ arg system, arrFileNames, winColour, inLeft, inTop;
		var btnCancel, btnOpenMostRecent, btnOpenNew, btnClearHistory, popUpFileNames, isEmpty, firstItem;
		{
			winColour = winColour ? TXColour.sysLabelBackground;
			w = Window("Open Recent File", Rect(inLeft, inTop, defaultSize.x, defaultSize.y));
			w.front;
			w.alwaysOnTop_(true);
			w.view.decorator = FlowLayout(w.view.bounds);
			w.view.background = winColour;

			//w.view.decorator.shift(10,0);

			// OpenMostRecent
			btnOpenMostRecent = Button(w, 160 @ 24)
			.states = [["Open Most Recent File", TXColor.white, TXColor.sysGuiCol2]];
			btnOpenMostRecent.action = {
				if (arrFileNames.asArray.size > 0, {
					system.loadFileName(arrFileNames[0]);
				});
				w.close;
			};

			w.view.decorator.shift(30,0);

			// OpenNew
			btnOpenNew = Button(w, 100 @ 24)
			.states = [["Open New File", TXColor.white, TXColor.sysGuiCol1]];
			btnOpenNew.action = {
				var newPath, newFile, newString, newData;
				w.close;
				if (TXSystem1.server.serverRunning.not, {TXSystem1.server.boot});
				Dialog.openPanel({ arg path;
					// reload system settings
					TXSystem1.loadSystemSettings;
					TXSystem1GUI.setWindowImage;
					TXSystem1.loadFileName(path);
				});
			};

			w.view.decorator.shift(30,0);

			// cancel button
			btnCancel = Button(w, 60 @ 24)
			.states = [["Cancel", TXColor.white, TXColor.grey]];
			btnCancel.action = {w.close;};

			arrFileNames = arrFileNames ? [];
			if (arrFileNames.size == 0, {
				isEmpty = true;
				firstItem = "< Empty >"
			}, {
				isEmpty = false;
				firstItem = "Select a file to open ..."
			});

			w.view.decorator.shift(30,0);

			// clear history
			btnClearHistory = Button(w, 100 @ 24)
			.states_([["Clear History", TXColor.white, TXColor.sysDeleteCol]])
			.action_({
				system.clearRecentFileNames;
				popUpFileNames.items = ["< Empty >"];
				isEmpty = true;
				arrFileNames = [];
			});

			w.view.decorator.nextLine;

			popUpFileNames = TXListView(w, 800 @ 290).resize_(5); // stretchable
			popUpFileNames.background = TXColor.white;
			popUpFileNames.stringColor = TXColor.black;
			popUpFileNames.items = [firstItem] ++ arrFileNames;
			popUpFileNames.action = {arg view;
				w.alwaysOnTop_(false);
				if ((arrFileNames.size > 0) && (view.value > 0), {
					system.loadFileName(arrFileNames[view.value - 1]);
				});
				w.close;
			};
			w.view.decorator.shift(0,10);
		}.defer;
	}

}
