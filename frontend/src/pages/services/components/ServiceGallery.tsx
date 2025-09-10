import { useState } from 'react';
import { Box } from '@mui/material';

interface ServiceGalleryProps {
  images: string[];
}

const ServiceGallery = ({ images }: ServiceGalleryProps) => {
  const [selectedImage, setSelectedImage] = useState(0);

  return (
    <Box>
      {/* Main Image */}
      <Box 
        sx={{
          width: '100%',
          height: { xs: 300, md: 400 },
          borderRadius: 2,
          overflow: 'hidden',
          mb: 2,
          bgcolor: 'background.paper',
          '& img': {
            width: '100%',
            height: '100%',
            objectFit: 'cover',
          }
        }}
      >
        <img 
          src={images[selectedImage]} 
          alt={`Service ${selectedImage + 1}`} 
        />
      </Box>

      {/* Thumbnails */}
      {images.length > 1 && (
        <Box 
          sx={{ 
            display: 'flex', 
            gap: 1.5,
            overflowX: 'auto',
            py: 1,
            '&::-webkit-scrollbar': {
              display: 'none',
            },
            msOverflowStyle: 'none',
            scrollbarWidth: 'none',
          }}
        >
          {images.map((img, index) => (
            <Box
              key={index}
              onClick={() => setSelectedImage(index)}
              sx={{
                flexShrink: 0,
                width: 80,
                height: 60,
                borderRadius: 1,
                overflow: 'hidden',
                cursor: 'pointer',
                border: selectedImage === index 
                  ? '2px solid primary.main' 
                  : '1px solid',
                borderColor: selectedImage === index 
                  ? 'primary.main' 
                  : 'divider',
                opacity: selectedImage === index ? 1 : 0.7,
                transition: 'all 0.2s',
                '&:hover': {
                  opacity: 1,
                  transform: 'scale(1.05)',
                },
                '& img': {
                  width: '100%',
                  height: '100%',
                  objectFit: 'cover',
                }
              }}
            >
              <img 
                src={img} 
                alt={`Thumbnail ${index + 1}`}
              />
            </Box>
          ))}
        </Box>
      )}
    </Box>
  );
};

export default ServiceGallery;
