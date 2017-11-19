function [pointMat] = tranform113(filetxt)
in1 = load(filetxt);

bs=size(in1);
rows=bs(1);
%output1=zeros(rows,13);
pointMat=zeros(rows,2);


for i=1:rows
   [~,~,~,~,~,~,~,~,~,~,~,~,exit3]=tranform1112(in1(i,:));
   pointMat(i,:)=exit3; 
end
scatter( pointMat(:,1), pointMat(:,2),5,'red');
   
end

