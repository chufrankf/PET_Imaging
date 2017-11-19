function [output41] = tranform111(filetxt)
in1 = load(filetxt);
%TRANFORM1 
%[a,b,c,d,e,f]=tranform1(input3,20,20,3)
%in1 is a table of [angles, smallest distant form the center]

%initialize output1, [m's,b's] (y=mx+b)
bs=size(in1);
rows=bs(1);
output1=zeros(bs);

% transfotm in1 to output1
%m=tan(angle)
%distant=abs(y1-mx1-b)/sqrt(m^2+1)
%x1=y1=0
for n = 1:rows
    angle=in1(n,1);
    distant=in1(n,2);
    if angle > 90 || angle < -90
        errordlg('Angle out of rage','Input Error');
    elseif angle == 90
        angle=89.99999;
    elseif angle == -90
        angle=-89.99999;
    end
    output1(n,1)=tand(angle);
    output1(n,2)=distant*sqrt((output1(n,1))^2+1);
end


output3=0;
output4=zeros((rows-1)*(rows)/factorial(2),2);
for i = 1:rows
    for j = i+1:rows
      detmatrix=-output1(i,1)+output1(j,1);
        if detmatrix~=0
          output3=output3+1;
          bss=[output1(i,2);output1(j,2)];
          inverse=1/(detmatrix)*[1,-1;output1(j,1),-output1(i,1)];
          output4(output3,:)=round(transpose(inverse*bss));
        end
    end
end

%clean
output4=output4(1:output3,:);
bs=size(output4);
rows=bs(1);
output41=zeros(size(output4));
k=1;

for i=1:rows
   if output4(i,1)>-500 && output4(i,1)<500 && output4(i,2)>-300 && output4(i,2)<300
   output41(k,:)=output4(i,:);
   k=k+1;
   end
end

output41=output41(1:k-1,:);   


%figure
%scatter(output41(:,1), output41(:,2),0.0000000000000001,'red');

%http://www.mathworks.com/company/newsletters/articles/accelerating-matlab-algorithms-and-applications.html
%histogram
%http://www.mathworks.com/matlabcentral/fileexchange/45325-efficient-2d-histogram--no-toolboxes-needed/content/ndhist.m
%Bitmap construction

%output5=zeros(ymax,xmax);
%xlimit=round(xmax/2);
%ylimit=round(ymax/2);
%for i=1:output3
%    if ylimit-output4(i,2)>0 && xlimit+output4(i,1)>0
%    output5(ylimit-output4(i,2),xlimit+output4(i,1))=1;
%    end
%end
%figure
%imshow(output5);


%Threshole

%output6=sortrows(output4);

%zzz=1;
%for z = 1:output3
%    counter=1;
%      for zz = z+1:output3
%             if output6(z,:) == output6(zz,:)
%             counter=counter+1;
%                 if counter == th
%                    output7(zzz,:)=output6(z,:);
%                    zzz=zzz+1;
%                 end
%             end
%       end
%end
  

%bitmap after threshold


%output8=zeros(ymax,xmax);
%sizeth=size(output7);
%for i=1:sizeth(1)
%    output8(ylimit-output7(i,2),xlimit+output7(i,1))=1;
 
%end
%figure
%imshow(output8);

end

