close all;
clear all;

fprintf('Doing all actions, read, shift, clean_tfilt \n');

invRead;
sbmat = shift(allbmatrix);

fprintf('\nObtainting Threshold Filtered Matrix for non-shifted allbmatrix \n');
tfiltmat = clean_tfilt(allbmatrix);
fprintf('\nObtainting Threshold Filtered Matrix for shifted allbmatrix \n');
tsfiltmat = clean_tfilt(sbmat);


fprintf('\ninitallbmat: Raw data (no changes) \nallbmatrix: Matrix After removing/replacing/ignoring infinite values \nnames: Names of Data folders you placed in read Directory (use for graphVoltHist and graphRiseHist) \nsbmat: Cell after shift has been made (no threshold) \ntfiltbmat: Cell with threshold filtering with no prior shift \ntsfiltbmat: Cell with threshold filtering after a shift\n');
