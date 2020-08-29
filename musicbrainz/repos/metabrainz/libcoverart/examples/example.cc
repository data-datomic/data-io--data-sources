/* --------------------------------------------------------------------------

   libcoverart - Client library to access MusicBrainz

   Copyright (C) 2012 Andrew Hawkins

   This file is part of libcoverart.

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   libcoverart is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library.  If not, see <http://www.gnu.org/licenses/>.

     $Id$

----------------------------------------------------------------------------*/

#include <iostream>
#include <sstream>
#include <fstream>

#include "coverart/CoverArt.h"
#include "coverart/ReleaseInfo.h"
#include "coverart/ImageList.h"
#include "coverart/Image.h"
#include "coverart/Thumbnails.h"
#include "coverart/HTTPFetch.h"
#include "coverart/TypeList.h"
#include "coverart/Type.h"

int main(int argc, const char *argv[])
{
	if (argc==2)
	{
		std::string ReleaseID=argv[1];

		CoverArtArchive::CCoverArt CoverArt("example-1.0");

		try
		{
			std::cout << "Fetching front" << std::endl;
			std::vector<unsigned char> ImageData=CoverArt.FetchFront(ReleaseID);
			if (ImageData.size())
			{
				std::stringstream FileName;
				FileName << ReleaseID << "-front.jpg";
				std::cout << "Saving front to '" << FileName.str() << "'" << std::endl;

				std::ofstream Front(FileName.str().c_str());
				Front.write((const char *)&ImageData[0],ImageData.size());
				Front.close();
			}
		}

		catch (const CoverArtArchive::CExceptionBase& e)
		{
			std::cout << "Exception: '" << e.what() << "'" << std::endl;
		}

		try
		{
			std::cout << "Fetching back" << std::endl;
			std::vector<unsigned char> ImageData=CoverArt.FetchBack(ReleaseID);
			if (ImageData.size())
			{
				std::stringstream FileName;
				FileName << ReleaseID << "-back.jpg";
				std::cout << "Saving back to '" << FileName.str() << "'" << std::endl;

				std::ofstream Back(FileName.str().c_str());
				Back.write((const char *)&ImageData[0],ImageData.size());
				Back.close();
			}
		}

		catch (const CoverArtArchive::CExceptionBase& e)
		{
			std::cout << "Exception: '" << e.what() << "'" << std::endl;
		}

		CoverArtArchive::CReleaseInfo ReleaseInfo=CoverArt.ReleaseInfo(ReleaseID);
		std::cout << ReleaseInfo << std::endl;
		return 0;

		CoverArtArchive::CImageList *ImageList=ReleaseInfo.ImageList();
		if (ImageList)
		{
			std::cout << "Found " << ImageList->NumItems() << " images";

			for (int count=0;count<ImageList->NumItems();count++)
			{
				CoverArtArchive::CImage *Image=ImageList->Item(count);
				if (Image)
				{
					std::cout << std::endl;
					std::cout << "Approved: " << std::boolalpha << Image->Approved() << std::endl;
					std::cout << "Back: " << std::boolalpha << Image->Back() << std::endl;
					std::cout << "Comment: " << Image->Comment() << std::endl;
					std::cout << "Edit: " << Image->Edit() << std::endl;
					std::cout << "Front: " << std::boolalpha << Image->Front() << std::endl;
					std::cout << "ID: " << Image->ID() << std::endl;
					std::cout << "Image: " << Image->Image() << std::endl;

					CoverArtArchive::CTypeList *TypeList=Image->TypeList();
					if (TypeList)
					{
						for (int count=0; count<TypeList->NumItems();count++)
						{
							CoverArtArchive::CType *Type=TypeList->Item(count);
							if (Type)
								std::cout << "Type: " << Type->Type() << std::endl;
						}
					}

					try
					{
						std::cout << "Fetching full size for image '" << Image->ID() << "'" << std::endl;
						std::vector<unsigned char> ImageData=CoverArt.FetchImage(ReleaseID,Image->ID());

						std::stringstream FileName;
						FileName << ReleaseID << "-" << Image->ID() << "-full.jpg";
						std::cout << "Saving image to '" << FileName.str() << "'" << std::endl;

						std::ofstream Full(FileName.str().c_str());
						Full.write((const char *)&ImageData[0],ImageData.size());
						Full.close();
					}

					catch (const CoverArtArchive::CExceptionBase& e)
					{
						std::cout << "Exception: '" << e.what() << "'" << std::endl;
					}

					CoverArtArchive::CThumbnails *Thumbnails=Image->Thumbnails();
					if (Thumbnails)
					{
						if (!Thumbnails->Large().empty())
						{
							try
							{
								std::cout << "Fetching large for image '" << Image->ID() << "'" << std::endl;
								std::vector<unsigned char> ImageData=CoverArt.FetchImage(ReleaseID,Image->ID(),CoverArtArchive::CCoverArt::eSize_500);

								std::stringstream FileName;
								FileName << ReleaseID << "-" << Image->ID() << "-large.jpg";
								std::cout << "Saving image to '" << FileName.str() << "'" << std::endl;

								std::ofstream Large(FileName.str().c_str());
								Large.write((const char *)&ImageData[0],ImageData.size());
								Large.close();
							}

							catch (const CoverArtArchive::CExceptionBase& e)
							{
								std::cout << "Exception: '" << e.what() << "'" << std::endl;
							}
						}

						if (!Thumbnails->Small().empty())
						{
							try
							{
								std::cout << "Fetching small for image '" << Image->ID() << "'" << std::endl;
								std::vector<unsigned char> ImageData=CoverArt.FetchImage(ReleaseID,Image->ID(),CoverArtArchive::CCoverArt::eSize_250);

								std::stringstream FileName;
								FileName << ReleaseID << "-" << Image->ID() << "-small.jpg";
								std::cout << "Saving image to '" << FileName.str() << "'" << std::endl;

								std::ofstream Small(FileName.str().c_str());
								Small.write((const char *)&ImageData[0],ImageData.size());
								Small.close();
							}

							catch (const CoverArtArchive::CExceptionBase& e)
							{
								std::cout << "Exception: '" << e.what() << "'" << std::endl;
							}
						}
					}
				}
			}
		}
		else
			std::cout << "No images found" << std::endl;
	}
	else
	{
		std::cerr << "Usage: " << argv[0] << " releaseid" << std::endl;
	}

	return 0;
}
